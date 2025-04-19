package br.com.scraper.selenium

import br.com.scraper.config.WebDriverProperties
import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class WebDriverFactory(
    private val webDriverProperties: WebDriverProperties
) {

    private val logger = LoggerFactory.getLogger(WebDriverFactory::class.java)

    fun create(): WebDriver {
        WebDriverManager.chromedriver().setup()

        val options = ChromeOptions()
        options.addArguments(webDriverProperties.arguments)
        val randomUserAgent = webDriverProperties.userAgents.random()
        options.addArguments("--user-agent=$randomUserAgent")

        logger.info("Usando User-Agent: $randomUserAgent")

        return ChromeDriver(options)
    }
}
