package br.com.scraper.core.strategy

interface WaitMechanism {
    fun waitFor(milliseconds: Long)
}