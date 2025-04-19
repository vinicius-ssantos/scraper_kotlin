package br.com.scraper.infrastructure.strategy

import br.com.scraper.core.strategy.DelayStrategy
import kotlin.math.pow

class ExponentialDelayStrategy(private val baseDelay: Long) : DelayStrategy {
    override fun calculateDelay(attempt: Int): Long {
        return baseDelay * 2.0.pow(attempt.toDouble()).toLong()
    }
}