package br.com.scraper.utils

import org.slf4j.LoggerFactory

class RetryUtils(
    private val delayStrategy: DelayStrategy,
    private val waitMechanism: WaitMechanism
) {
    private val logger = LoggerFactory.getLogger(RetryUtils::class.java)

    fun <T> executeWithRetry(
        maxRetries: Int = 3,
        retryOn: List<Class<out Exception>> = listOf(Exception::class.java),
        onFailure: ((attempt: Int, exception: Exception) -> Unit)? = null,
        operation: () -> T
    ): T {
        var attempt = 0
        var lastException: Exception? = null

        while (attempt < maxRetries) {
            try {
                logger.info("Tentativa ${attempt + 1}/$maxRetries...")
                return operation()
            } catch (e: Exception) {
                if (retryOn.none { it.isInstance(e) }) throw e
                lastException = e
                onFailure?.invoke(attempt + 1, e)

                val delay = delayStrategy.calculateDelay(attempt)
                logger.warn("Erro na tentativa ${attempt + 1}: ${e.message}. Aguardando ${delay}ms para nova tentativa.")
                waitMechanism.waitFor(delay)
                attempt++
            }
        }

        logger.error("Todas as tentativas falharam após $maxRetries tentativas.", lastException)
        throw lastException ?: RuntimeException("Erro desconhecido no retry")
    }
}