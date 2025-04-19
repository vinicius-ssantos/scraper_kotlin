package br.com.scraper.config

import br.com.scraper.utils.DelayStrategy
import br.com.scraper.utils.ExponentialDelayStrategy
import br.com.scraper.utils.ThreadSleepWaitMechanism
import br.com.scraper.utils.WaitMechanism
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