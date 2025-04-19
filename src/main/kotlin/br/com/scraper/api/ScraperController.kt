package br.com.scraper.api

import br.com.scraper.model.Product
import br.com.scraper.service.ScraperService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/scrape")
class ScraperController(
    private val scraperService: ScraperService // interface
) {

    @GetMapping
    fun scrape(@RequestParam url: String): List<Product> {
        return scraperService.startScraping(url)
    }
}
