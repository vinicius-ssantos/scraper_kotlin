package br.com.scraper.infrastructure.selenium


import br.com.scraper.config.WebDriverConfig
import br.com.scraper.core.strategy.DelayStrategy

import br.com.scraper.core.strategy.WaitMechanism
import br.com.scraper.infrastructure.strategy.DefaultRetryStrategy
import org.openqa.selenium.WebDriver
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class SeleniumSessionManager(
    private val webDriverConfig: WebDriverConfig,
    private val delayStrategy: DelayStrategy,
    private val waitMechanism: WaitMechanism
) {

    private val logger = LoggerFactory.getLogger(SeleniumSessionManager::class.java)

    fun <T> useSession(action: (WebDriver) -> T): T {
        val driver = webDriverConfig.create()

        return try {
            val retryStrategy = DefaultRetryStrategy(delayStrategy, waitMechanism)
            retryStrategy.execute {
                action(driver)
            }
        } catch (ex: Exception) {
            logger.error("Erro durante a sessão Selenium", ex)
            throw ex
        } finally {
            logger.info("Encerrando sessão WebDriver")
            driver.quit()
        }
    }
}