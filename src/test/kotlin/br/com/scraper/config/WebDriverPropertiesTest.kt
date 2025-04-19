package br.com.scraper.config

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource

class WebDriverPropertiesTest {

 @Test
 fun `deve mapear propriedades corretamente`() {
  // Simula as propriedades do arquivo application.yml
  val properties = mapOf(
   "webdriver.userAgents[0]" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36",
   "webdriver.userAgents[1]" to "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36",
   "webdriver.arguments[0]" to "--headless",
   "webdriver.arguments[1]" to "--disable-gpu"
  )

  // Cria uma fonte de propriedades simulada
  val source = MapConfigurationPropertySource(properties)
  val binder = Binder(source)

  // Faz o bind das propriedades para a classe WebDriverProperties
  val webDriverProperties = binder.bind("webdriver", WebDriverProperties::class.java).get()

  // Verifica se as propriedades foram mapeadas corretamente
  assertEquals(2, webDriverProperties.userAgents.size)
  assertEquals("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36", webDriverProperties.userAgents[0])
  assertEquals("--headless", webDriverProperties.arguments[0])
 }
}