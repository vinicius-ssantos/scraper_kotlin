package br.com.scraper.core.runner

interface ScraperJobFactory {
    fun create(context: JobContext): ScraperJob
}
