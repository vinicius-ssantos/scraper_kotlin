package br.com.scraper.utils

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.slf4j.LoggerFactory

object SelectorLoader {
    private val logger = LoggerFactory.getLogger(SelectorLoader::class.java)

    fun load(fileName: String): Map<String, List<String>> {
        val path = "selectors/$fileName.yaml"
        val resource = SelectorLoader::class.java.classLoader.getResource(path)
            ?: throw IllegalArgumentException("Arquivo de seletores não encontrado: $path")

        logger.info("Carregando seletores do arquivo YAML: $path")

        return try {
            val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
            mapper.readValue(resource, object : TypeReference<Map<String, List<String>>>() {})
        } catch (e: Exception) {
            logger.error("Erro ao carregar seletores de $path: ${e.message}", e)
            throw e
        }
    }
}
