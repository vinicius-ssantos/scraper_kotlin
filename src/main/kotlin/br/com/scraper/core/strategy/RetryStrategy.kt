package br.com.scraper.core.strategy

interface RetryStrategy {
    fun <T> execute(operation: () -> T): T
}