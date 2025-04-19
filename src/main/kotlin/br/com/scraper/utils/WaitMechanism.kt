package br.com.scraper.utils

interface WaitMechanism {
    fun waitFor(milliseconds: Long)
}

class ThreadSleepWaitMechanism : WaitMechanism {
    override fun waitFor(milliseconds: Long) {
        Thread.sleep(milliseconds)
    }
}