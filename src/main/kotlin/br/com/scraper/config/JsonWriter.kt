package br.com.scraper.config

import br.com.scraper.model.Product
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object JsonWriter {

    private val objectMapper = ObjectMapper().registerKotlinModule()
    private val outputDir = "output"

    fun write(products: List<Product>) {
        if (products.isEmpty()) return

        Files.createDirectories(Paths.get(outputDir))

        val now = LocalDateTime.now()
        val timestamp = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))
        val filename = "$outputDir/products_info_$timestamp.json"

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(File(filename), products)
    }
}
