package br.com.scraper.utils

class DefaultRetryStrategy(
    private val delayStrategy: DelayStrategy,
    private val waitMechanism: WaitMechanism
) {
    fun <T> execute(maxRetries: Int = 3, operation: () -> T): T {
        var attempt = 0
        while (true) {
            try {
                return operation()
            } catch (e: Exception) {
                attempt++
                if (attempt >= maxRetries) throw e
                val delay = delayStrategy.calculateDelay(attempt)
                waitMechanism.waitFor(delay)
            }
        }
    }
}