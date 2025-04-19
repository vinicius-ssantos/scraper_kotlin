package br.com.scraper.core.strategy

interface DelayStrategy {
    fun calculateDelay(attempt: Int): Long
}