package br.com.scraper

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["br.com.scraper"])
open class ScraperApiApplication

fun main(args: Array<String>) {
    runApplication<ScraperApiApplication>(*args)
}
