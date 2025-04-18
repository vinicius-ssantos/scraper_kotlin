package br.com.scraper.selector

import org.slf4j.LoggerFactory
import org.yaml.snakeyaml.Yaml
import java.io.InputStream

object SelectorLoader {
    private val logger = LoggerFactory.getLogger(SelectorLoader::class.java)

    fun load(domain: String): Map<String, List<String>> {
        val fileName = "selectors_$domain.yaml"
        val inputStream: InputStream = javaClass.classLoader.getResourceAsStream(fileName)
            ?: throw IllegalArgumentException("Arquivo de seletores não encontrado: $fileName")

        logger.info("Carregando seletores de: $fileName")

        val yaml = Yaml()
        val data = yaml.load<Map<String, List<String>>>(inputStream)

        logger.info("Seletores carregados: ${data.keys}")
        return data
    }
}
