package br.com.scraper.infrastructure.runner

import br.com.scraper.core.ports.ScraperService
import br.com.scraper.core.runner.JobContext
import br.com.scraper.core.runner.ScraperJob
import br.com.scraper.core.runner.ScraperJobFactory
import org.springframework.stereotype.Component

@Component
class DefaultScraperJobFactory(
    private val scraperService: ScraperService
) : ScraperJobFactory {

    override fun create(context: JobContext): ScraperJob {
        return ScraperJobImpl(context, scraperService)
    }
}
