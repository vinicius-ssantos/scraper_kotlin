package br.com.scraper
import br.com.scraper.config.JsonWriter
import br.com.scraper.service.AmazonScraperService
import org.slf4j.LoggerFactory




    fun main() {
        val logger = LoggerFactory.getLogger("ScraperApplication")
        logger.info("Iniciando o Scraper da Amazon...")

        val scraper = AmazonScraperService()
        val url = "https://www.amazon.com.br/s?k=Displayport+KVM+Switch"

        val products = scraper.startScraping(url)
        products.forEach { logger.info("Produto: $it") }

        JsonWriter.write(products)
        logger.info("Finalizado. ${products.size} produtos salvos.")
    }

