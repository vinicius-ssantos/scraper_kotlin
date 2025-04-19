package br.com.scraper.service

import br.com.scraper.utils.JsonWriter
import br.com.scraper.model.Product
import br.com.scraper.selenium.SeleniumSessionManager
import br.com.scraper.utils.HtmlWriter
import br.com.scraper.utils.SelectorLoader
import br.com.scraper.utils.DelayStrategy
import br.com.scraper.utils.WaitMechanism
import br.com.scraper.utils.DefaultRetryStrategy
import jakarta.annotation.PostConstruct
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat
import java.util.*

@Service
class AmazonScraperService(
    private val jsonWriter: JsonWriter,
    private val seleniumSessionManager: SeleniumSessionManager,
    private val delayStrategy: DelayStrategy,
    private val waitMechanism: WaitMechanism
) : ScraperService {

    private val logger = LoggerFactory.getLogger(AmazonScraperService::class.java)
    private lateinit var selectors: Map<String, List<String>>

    @PostConstruct
    fun init() {
        selectors = SelectorLoader.load("selectors_amazon")
    }

    override fun startScraping(url: String): List<Product> {
        logger.info("Iniciando scraping da URL: $url")
        val products = mutableListOf<Product>()
        seleniumSessionManager.useSession { driver: WebDriver ->
            val retryStrategy = DefaultRetryStrategy(delayStrategy, waitMechanism)
            retryStrategy.execute {
                driver.get(url)

                savePageHtml(driver, "debug_page.html")

                val blockSelectors = selectors["product_block"] ?: emptyList()
                var productElements: List<WebElement> = emptyList()
                for (selector in blockSelectors) {
                    productElements = driver.findElements(By.cssSelector(selector))
                    if (productElements.isNotEmpty()) break
                }
                if (productElements.isEmpty()) {
                    logger.warn("Nenhum seletor de product_block funcionou.")
                    return@execute
                }

                logger.info("Foram encontrados ${productElements.size} blocos de produto.")

                for (element in productElements) {
                    val title = findTextInElement(element, selectors["title"])
                    val price = findTextInElement(element, selectors["price"])
                    val rating = findTextInElement(element, selectors["rating"])
                    val reviews = findTextInElement(element, selectors["reviews"])
                    val asin = element.getAttribute("data-asin") ?: "Indisponível"

                    products.add(Product(asin, title, price, rating, reviews))
                }
            }
        }

        jsonWriter.write(products)
        return products
    }

    private fun savePageHtml(driver: WebDriver, baseFileName: String) {
        try {
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val formattedFileName = "${baseFileName}_$timestamp.html"
            val htmlContent = driver.pageSource
            HtmlWriter.saveHtml(htmlContent, formattedFileName)
        } catch (e: Exception) {
            logger.error("Erro ao salvar o HTML da página: ${e.message}", e)
        }
    }

    private fun findTextInElement(element: WebElement, possibleSelectors: List<String>?): String {
        if (possibleSelectors == null) return "Indisponível"
        for (selector in possibleSelectors) {
            try {
                val found = element.findElement(By.cssSelector(selector))
                return found.text.trim().ifEmpty { "Indisponível" }
            } catch (_: Exception) {
            }
        }
        return "Indisponível"
    }
}