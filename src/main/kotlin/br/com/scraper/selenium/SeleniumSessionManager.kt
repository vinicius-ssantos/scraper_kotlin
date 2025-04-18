package br.com.scraper.selenium

import org.openqa.selenium.WebDriver
import org.slf4j.LoggerFactory

class SeleniumSessionManager {

    private val logger = LoggerFactory.getLogger(SeleniumSessionManager::class.java)

    fun <T> useSession(action: (WebDriver) -> T): T {
        val driver = WebDriverFactory.create()
        return try {
            action(driver)
        } catch (ex: Exception) {
            logger.error("Erro durante a sessão Selenium", ex)
            throw ex
        } finally {
            logger.info("Encerrando sessão WebDriver")
            driver.quit()
        }
    }
}
