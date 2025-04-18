package br.com.scraper.selector

import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Paths

object SelectorLoader {

    private val logger = LoggerFactory.getLogger(SelectorLoader::class.java)

    fun load(domain: String): Map<String, String> {
        val filename = "selectors_${domain}.txt"
        val path = Paths.get("src/main/resources", filename).toFile()

        if (!path.exists()) {
            logger.warn("Arquivo de seletores [$filename] não encontrado.")
            return emptyMap()
        }

        return path.readLines()
            .filter { it.isNotBlank() && it.contains("=") }
            .associate {
                val (key, value) = it.split("=", limit = 2)
                key.trim() to value.trim()
            }.also {
                logger.info("Seletores carregados de [$filename]: ${it.keys}")
            }
    }
}
