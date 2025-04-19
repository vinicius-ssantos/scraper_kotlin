import br.com.scraper.selenium.WebDriverFactory
import br.com.scraper.utils.DelayStrategy
import br.com.scraper.utils.RetryUtils
import br.com.scraper.utils.WaitMechanism
import org.openqa.selenium.WebDriver
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class SeleniumSessionManager(
    private val webDriverFactory: WebDriverFactory,
    private val delayStrategy: DelayStrategy,
    private val waitMechanism: WaitMechanism
) {

    private val logger = LoggerFactory.getLogger(SeleniumSessionManager::class.java)

    fun <T> useSession(action: (WebDriver) -> T): T {
        val driver = webDriverFactory.create()

        return try {
            val retryUtils = RetryUtils(delayStrategy, waitMechanism)
            retryUtils.executeWithRetry {
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