package br.com.scraper.utils

class ThreadSleepWaitMechanism : WaitMechanism {
    override fun waitFor(milliseconds: Long) {
        Thread.sleep(milliseconds)
    }
}