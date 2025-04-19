package br.com.scraper.infrastructure.runner

import br.com.scraper.core.ports.ScraperService
import br.com.scraper.core.runner.JobContext
import br.com.scraper.core.runner.ScraperJob
import org.slf4j.LoggerFactory

class ScraperJobImpl(
    private val context: JobContext,
    private val scraperService: ScraperService
) : ScraperJob {

    private val logger = LoggerFactory.getLogger(ScraperJobImpl::class.java)

    override fun execute() {
        logger.info("Iniciando job ${context.id} para URL: ${context.url}")
        scraperService.startScraping(context.url)
        logger.info("Finalizando job ${context.id}")
    }
}
