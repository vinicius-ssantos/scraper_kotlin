package br.com.scraper.utils

import br.com.scraper.model.Product
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Component
class JsonWriter(
    @Value("\${output.directory:output}") private val outputDir: String
) {

    private val logger = LoggerFactory.getLogger(JsonWriter::class.java)
    private val objectMapper = ObjectMapper().registerModule(KotlinModule.Builder().build())

    fun write(products: List<Product>) {
        if (products.isEmpty()) {
            logger.warn("Nenhum produto para salvar em JSON.")
            return
        }

        try {
            val outputDirectory = File(outputDir)
            if (!outputDirectory.exists() && !outputDirectory.mkdirs()) {
                throw IllegalStateException("Não foi possível criar o diretório de saída: $outputDir")
            }

            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val fileName = "$outputDir/products_$timestamp.json"

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(File(fileName), products)

            logger.info("Dados salvos em JSON: $fileName")
        } catch (e: Exception) {
            logger.error("Erro ao salvar os dados em JSON: ${e.message}", e)
            throw e
        }
    }
}