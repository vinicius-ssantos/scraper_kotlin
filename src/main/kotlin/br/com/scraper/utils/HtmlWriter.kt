package br.com.scraper.utils

import org.slf4j.LoggerFactory
import java.io.File

object HtmlWriter {

    private val logger = LoggerFactory.getLogger(HtmlWriter::class.java)

    fun saveHtml(content: String, fileName: String, outputDir: String = "output/html") {
        try {
            val directory = File(outputDir)
            if (!directory.exists() && !directory.mkdirs()) {
                throw IllegalStateException("Não foi possível criar o diretório: $outputDir")
            }

            val file = File(directory, fileName)
            file.writeText(content)
            logger.info("HTML salvo em: ${file.absolutePath}")
        } catch (e: Exception) {
            logger.error("Erro ao salvar o HTML: ${e.message}", e)
            throw e
        }
    }
}