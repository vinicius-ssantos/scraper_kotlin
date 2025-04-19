package br.com.scraper.config

import br.com.scraper.core.strategy.DelayStrategy
import br.com.scraper.infrastructure.strategy.ExponentialDelayStrategy
import br.com.scraper.infrastructure.strategy.ThreadSleepWaitMechanism
import br.com.scraper.core.strategy.WaitMechanism
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class AppConfig {

    @Bean
    open fun delayStrategy(): DelayStrategy {
        return ExponentialDelayStrategy(baseDelay = 1000L)
    }

    @Bean
    open  fun waitMechanism(): WaitMechanism {
        return ThreadSleepWaitMechanism()
    }
}