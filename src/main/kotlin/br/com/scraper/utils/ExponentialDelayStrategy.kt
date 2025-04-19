package br.com.scraper.utils

import kotlin.math.pow

class ExponentialDelayStrategy(private val baseDelay: Long) : DelayStrategy {
    override fun calculateDelay(attempt: Int): Long {
        return baseDelay * 2.0.pow(attempt.toDouble()).toLong()
    }
}