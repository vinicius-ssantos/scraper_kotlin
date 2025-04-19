package br.com.scraper.core.ports

import br.com.scraper.core.model.Product

interface ScraperService {
    fun startScraping(url: String): List<Product>
}
