package br.com.scraper.service

import br.com.scraper.model.Product

interface ScraperService {
    fun startScraping(url: String): List<Product>
}
