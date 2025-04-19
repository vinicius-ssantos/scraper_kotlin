package br.com.scraper.utils

import br.com.scraper.model.Product
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File

class HtmlWriterTest {

 @Test
 fun `deve gerar arquivo HTML com lista de produtos`() {
  // Mock do diretório de saída
  val outputDir = "temp_output"

  // Lista de produtos de exemplo
  val products = listOf(
   Product("Produto 1", "10.0", "Categoria 1", "4.5", "100"),
   Product("Produto 2", "20.0", "Categoria 2", "4.0", "50")
  )

  // Executa o método de escrita
  HtmlWriter.saveHtml(buildHtml(products), "products.html", outputDir)

  // Verifica se o arquivo foi criado no diretório de saída
  val outputDirectory = File(outputDir)
  assertTrue(outputDirectory.exists(), "O diretório de saída deve ser criado")

  val files = outputDirectory.listFiles { _, name -> name.endsWith(".html") }
  assertTrue(!files.isNullOrEmpty(), "Deve haver pelo menos um arquivo HTML no diretório de saída")

  // Limpeza do diretório temporário
  files?.forEach { it.delete() }
  outputDirectory.delete()
 }

 private fun buildHtml(products: List<Product>): String {
  return """
            <html>
            <head><title>Produtos</title></head>
            <body>
                <h1>Lista de Produtos</h1>
                <ul>
                    ${products.joinToString("") { "<li>${it.asin} - ${it.price}</li>" }}
                </ul>
            </body>
            </html>
        """.trimIndent()
 }
}