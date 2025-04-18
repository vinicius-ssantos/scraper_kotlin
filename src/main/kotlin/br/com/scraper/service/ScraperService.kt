package br.com.scraper.service

import org.openqa.selenium.WebDriver

interface ScraperService {
    fun startScraping(driver: WebDriver, url: String): List<Map<String, String>>
}
