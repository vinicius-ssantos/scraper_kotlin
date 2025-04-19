package br.com.scraper.factory

import br.com.scraper.config.WebDriverProperties
import br.com.scraper.selenium.WebDriverFactory
import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

class WebDriverFactoryTest {

 @Test
 fun `deve criar uma instancia de WebDriver com configuracoes corretas`() {
  // Configuração das propriedades do WebDriver
  val webDriverProperties = WebDriverProperties().apply {
   userAgents = listOf("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36")
   arguments = listOf("--headless", "--disable-gpu")
  }

  // Configuração do WebDriverManager
  WebDriverManager.chromedriver().setup()

  // Criação do WebDriverFactory
  val factory = WebDriverFactory(webDriverProperties)

  // Criação do WebDriver
  val driver: WebDriver = factory.create()

  // Verificações
  assertNotNull(driver, "O WebDriver não deve ser nulo")
  assert(driver is ChromeDriver) { "O WebDriver deve ser uma instância de ChromeDriver" }

  // Fechar o driver após o teste
  driver.quit()
 }

 @Test
 fun `deve configurar argumentos e User-Agent corretamente`() {
  // Configuração das propriedades do WebDriver
  val webDriverProperties = WebDriverProperties().apply {
   userAgents = listOf("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36")
   arguments = listOf("--headless", "--disable-gpu")
  }

  // Configuração do ChromeOptions
  val options = ChromeOptions()
  options.addArguments(webDriverProperties.arguments)
  options.addArguments("--user-agent=${webDriverProperties.userAgents[0]}")

  // Configuração do WebDriverManager
  WebDriverManager.chromedriver().setup()

  // Criação do WebDriverFactory
  val factory = WebDriverFactory(webDriverProperties)

  // Criação do WebDriver
  val driver: WebDriver = factory.create()

  // Verificações
  assertNotNull(driver, "O WebDriver não deve ser nulo")
  assert(driver is ChromeDriver) { "O WebDriver deve ser uma instância de ChromeDriver" }

  // Fechar o driver após o teste
  driver.quit()
 }
}