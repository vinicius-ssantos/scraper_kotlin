package br.com.scraper.infrastructure.strategy

import br.com.scraper.core.strategy.WaitMechanism

class ThreadSleepWaitMechanism : WaitMechanism {
    override fun waitFor(milliseconds: Long) {
        Thread.sleep(milliseconds)
    }
}