package br.com.scraper.service

import br.com.scraper.model.Product
import br.com.scraper.selector.SelectorLoader
import br.com.scraper.selenium.SeleniumSessionManager
import br.com.scraper.config.JsonWriter
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class AmazonScraperService(
    private val sessionManager: SeleniumSessionManager = SeleniumSessionManager()
) : ScraperService {

    private val logger = LoggerFactory.getLogger(AmazonScraperService::class.java)
    private val selectors = SelectorLoader.load("amazon")

    override fun startScraping(driver: WebDriver, url: String): List<Map<String, String>> {
        logger.info("Iniciando scraping da URL: $url")
        val products = mutableListOf<Product>()
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))

        try {
            driver.get(url)

            // Salvar HTML
            val htmlSource = driver.pageSource
            val htmlOutput = File("logs/html/html_debug_$timestamp.html")
            htmlOutput.writeText(htmlSource)
            logger.info("HTML salvo em: ${htmlOutput.absolutePath}")

            if (htmlSource.contains("algo deu errado", ignoreCase = true) ||
                htmlSource.contains("Desculpe! Algo deu errado", ignoreCase = true) ||
                htmlSource.contains("/error/500-title", ignoreCase = true)
            ) {
                logger.error("Página de bloqueio detectada! Scraping abortado.")
                return emptyList()
            }

            val productElements = getWithFallback(driver, selectors["product_block"] ?: listOf())
            if (productElements.isEmpty()) {
                logger.warn("Nenhum seletor de product_block funcionou.")
                return emptyList()
            }

            logger.info("Foram encontrados ${productElements.size} blocos de produto.")

            for ((index, element) in productElements.withIndex()) {
                try {
                    val title = extract(element, "title")
                    val price = extract(element, "price")
                    val rating = extract(element, "rating")
                    val reviews = extract(element, "reviews")
                    val asin = element.getAttribute("data-asin") ?: "Indisponível"

                    if (title == "Indisponível" && price == "Indisponível" &&
                        rating == "Indisponível" && reviews == "Indisponível"
                    ) {
                        logger.debug("Produto [$index] ignorado: todos os campos indisponíveis.")
                        continue
                    }

                    logger.debug("Produto [$index] HTML:\n" + element.getAttribute("outerHTML"))

                    products.add(Product(asin, title, price, rating, reviews))
                } catch (e: Exception) {
                    logger.warn("Erro ao processar produto [$index]: ${e.message}")
                }
            }

            if (products.isNotEmpty()) {
                JsonWriter.write(products)
                logger.info("Produtos salvos com sucesso.")
            }

        } catch (e: Exception) {
            logger.error("Erro durante o scraping: ${e.message}", e)
        }

        return products.map {
            mapOf(
                "asin" to it.asin,
                "title" to it.title,
                "price" to it.price,
                "rating" to it.rating,
                "reviews" to it.reviews
            )
        }
    }

    private fun extract(element: WebElement, key: String): String {
        val fallbackSelectors = selectors[key] ?: return "Indisponível"
        for (selector in fallbackSelectors) {
            try {
                val value = element.findElement(By.cssSelector(selector)).text.trim()
                if (value.isNotBlank()) return value
            } catch (_: Exception) {
                logger.debug("Seletor falhou: $key -> $selector")
            }
        }
        return "Indisponível"
    }

    private fun getWithFallback(driver: WebDriver, selectors: List<String>): List<WebElement> {
        val wait = WebDriverWait(driver, Duration.ofSeconds(20))
        for (selector in selectors) {
            try {
                val elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(selector)))
                if (elements.isNotEmpty()) return elements
            } catch (e: Exception) {
                logger.debug("Fallback de product_block falhou: $selector")
            }
        }
        return emptyList()
    }
}
