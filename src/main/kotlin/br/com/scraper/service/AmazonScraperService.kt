package br.com.scraper.service

import br.com.scraper.model.Product
import br.com.scraper.selector.SelectorLoader
import br.com.scraper.selenium.SeleniumSessionManager
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import org.slf4j.LoggerFactory
import java.time.Duration

class AmazonScraperService(
    private val sessionManager: SeleniumSessionManager = SeleniumSessionManager()
) {
    private val logger = LoggerFactory.getLogger(AmazonScraperService::class.java)
    private val selectors = SelectorLoader.load("amazon")

    fun startScraping(url: String): List<Product> {
        logger.info("Iniciando scraping da URL: $url")

        val products = mutableListOf<Product>()

        sessionManager.useSession { driver ->
            driver.get(url)

            val wait = WebDriverWait(driver, Duration.ofSeconds(20))
            val productSelector = selectors["product_block"]
                ?: error("Seletor 'product_block' não encontrado.")

            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(productSelector)))
            val productElements = driver.findElements(By.cssSelector(productSelector))

            logger.info("Foram encontrados ${productElements.size} blocos de produto.")

            for ((index, element) in productElements.withIndex()) {
                try {
                    val title = extract(element, "title")
                    val price = extract(element, "price")
                    val rating = extract(element, "rating")
                    val reviews = extract(element, "reviews")
                    val asin = element.getAttribute("data-asin") ?: "Indisponível"

                    logger.debug("HTML do produto [$index]: \n" + element.getAttribute("outerHTML"))

                    products.add(Product(asin, title, price, rating, reviews))
                } catch (e: Exception) {
                    logger.warn("Erro ao processar produto [$index]: ${e.message}")
                }
            }
        }

        return products
    }

    private fun extract(element: WebElement, key: String): String {
        val selector = selectors[key] ?: return "Indisponível"
        return try {
            element.findElement(By.cssSelector(selector)).text.trim()
        } catch (e: Exception) {
            logger.debug("Erro ao extrair '$key' com seletor '$selector': ${e.message}")
            "Indisponível"
        }
    }
}
