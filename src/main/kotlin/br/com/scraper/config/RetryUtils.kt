package br.com.scraper.config

import org.slf4j.LoggerFactory
import kotlin.math.pow
import kotlin.time.Duration.Companion.seconds

object RetryUtils {
    private val logger = LoggerFactory.getLogger(RetryUtils::class.java)

    fun <T> executeWithRetry(
        maxRetries: Int = 3,
        baseDelay: Long = 2L,
        operation: () -> T
    ): T {
        var attempt = 0
        var lastException: Exception? = null

        while (attempt < maxRetries) {
            try {
                logger.info("Tentativa ${attempt + 1}/$maxRetries...")
                return operation()
            } catch (e: Exception) {
                lastException = e
                val delay = baseDelay * 2.0.pow(attempt.toDouble()).toLong()
                logger.warn("Erro na tentativa ${attempt + 1}: ${e.message}. Aguardando $delay segundos para nova tentativa.")
                Thread.sleep(delay.seconds.inWholeMilliseconds)
                attempt++
            }
        }

        logger.error("Todas as tentativas falharam após $maxRetries tentativas.", lastException)
        throw lastException ?: RuntimeException("Erro desconhecido no retry")
    }
}
