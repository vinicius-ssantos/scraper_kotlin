package br.com.scraper.selenium

import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

object WebDriverFactory {

    private val userAgents = listOf(
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36",
        "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36"
    )

    fun create(): WebDriver {
        WebDriverManager.chromedriver().setup()

        val options = ChromeOptions().apply {
//            addArguments("--headless=new") // headless "new" mais estável
            addArguments("--disable-gpu")
            addArguments("--window-size=1920,1080")
            addArguments("--no-sandbox")
            addArguments("--disable-dev-shm-usage")
            addArguments("--user-agent=${userAgents.random()}")

        }

        return ChromeDriver(options)
    }
}
