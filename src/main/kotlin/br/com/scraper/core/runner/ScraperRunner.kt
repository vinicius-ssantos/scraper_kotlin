package br.com.scraper.core.runner

import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.stereotype.Component

@Component
class ScraperRunner(
    private val scraperJobFactory: ScraperJobFactory
) {
    private val logger = LoggerFactory.getLogger(ScraperRunner::class.java)

    fun runParallel(urls: List<String>) = runBlocking {
        coroutineScope {
            urls.mapIndexed { index, url ->
                val context = JobContext(id = index, url = url)
                val job = scraperJobFactory.create(context)

                launch(Dispatchers.IO) {
                    MDC.put("worker", "worker-${context.id}")
                    try {
                        job.execute()
                    } catch (e: Exception) {
                        logger.error("Erro no worker-${context.id}: ${e.message}", e)
                    } finally {
                        MDC.clear()
                    }
                }
            }.joinAll()
        }
    }
}
