package br.com.scraper.infrastructure.runner

import br.com.scraper.config.WebDriverConfig
import br.com.scraper.core.ports.ScraperService
import br.com.scraper.core.runner.JobContext
import br.com.scraper.core.runner.ScraperJob
import br.com.scraper.infrastructure.proxy.ProxyProvider
import org.slf4j.LoggerFactory


class ScraperJobImpl(
    private val context: JobContext,
    private val scraperService: ScraperService,
    private val proxyProvider: ProxyProvider,
    private val webDriverConfig: WebDriverConfig
) : ScraperJob {

    override fun execute() {
        val proxy = proxyProvider.getRandomProxy()
        val driver = webDriverConfig.create(proxy)

        // passar driver para sua lógica ou service
        // scraperService.startScrapingWith(driver, context.url)
    }
}
