package br.com.scraper.utils

import br.com.scraper.core.model.Product
import br.com.scraper.infrastructure.writer.JsonWriter
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File

class JsonWriterTest {

 @Test
 fun `deve escrever lista de produtos em arquivo JSON`() {
  // Mock do diretório de saída
  val outputDir = "temp_output"
  val jsonWriter = JsonWriter(outputDir)

  // Lista de produtos de exemplo
  val products = listOf(
   Product("Produto 1", "10.0", "Categoria 1", "4.5", "100"),
   Product("Produto 2", "20.0", "Categoria 2", "4.0", "50")
  )

  // Executa o método de escrita
  jsonWriter.write(products)

  // Verifica se o arquivo foi criado no diretório de saída
  val outputDirectory = File(outputDir)
  assertTrue(outputDirectory.exists(), "O diretório de saída deve ser criado")

  val files = outputDirectory.listFiles { _, name -> name.endsWith(".json") }
  assertTrue(!files.isNullOrEmpty(), "Deve haver pelo menos um arquivo JSON no diretório de saída")

  // Limpeza do diretório temporário
  files?.forEach { it.delete() }
  outputDirectory.delete()
 }
}