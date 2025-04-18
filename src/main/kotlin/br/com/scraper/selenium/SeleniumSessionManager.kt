package br.com.scraper.selenium

import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.slf4j.LoggerFactory

class SeleniumSessionManager {

    private val logger = LoggerFactory.getLogger(SeleniumSessionManager::class.java)
    private val userAgents = listOf(
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36",
        "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:109.0) Gecko/20100101 Firefox/115.0",
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 12_4) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.5 Safari/605.1.15",
        "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",
        "Mozilla/5.0 (iPhone; CPU iPhone OS 14_4_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148"
    )


    fun <T> useSession(action: (WebDriver) -> T): T {

        val options = ChromeOptions()
        val userAgent = userAgents.random()
        options.addArguments("--user-agent=$userAgent")
        options.addArguments("--disable-blink-features=AutomationControlled")
        val driver = ChromeDriver(options)

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
