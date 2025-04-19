package br.com.scraper.utils

interface RetryStrategy {
    fun <T> execute(operation: () -> T): T
}