package br.com.scraper.utils

interface DelayStrategy {
    fun calculateDelay(attempt: Int): Long
}