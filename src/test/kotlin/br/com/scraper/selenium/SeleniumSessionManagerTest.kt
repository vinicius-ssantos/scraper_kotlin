package br.com.scraper.selenium


import br.com.scraper.config.WebDriverConfig
import br.com.scraper.core.strategy.DelayStrategy
import br.com.scraper.core.strategy.WaitMechanism
import br.com.scraper.infrastructure.selenium.SeleniumSessionManager
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.openqa.selenium.WebDriver

class SeleniumSessionManagerTest {

 @Test
 fun `deve executar acao dentro da sessao e encerrar o WebDriver`() {
  // Mock do WebDriver
  val mockDriver = mock(WebDriver::class.java)

  // Mock do WebDriverFactory
  val mockFactory = mock(WebDriverConfig::class.java)
  `when`(mockFactory.create()).thenReturn(mockDriver)

  // Mock das dependências adicionais
  val mockDelayStrategy = mock(DelayStrategy::class.java)
  val mockWaitMechanism = mock(WaitMechanism::class.java)

  // Instância do SeleniumSessionManager com os mocks
  val sessionManager = SeleniumSessionManager(mockFactory, mockDelayStrategy, mockWaitMechanism)

  // Ação a ser executada
  val result: String = sessionManager.useSession { driver: WebDriver ->
   assertTrue(driver === mockDriver, "O WebDriver deve ser o mock fornecido")
   "resultado esperado"
  }

  // Verificações
  assertTrue(result == "resultado esperado", "O resultado da ação deve ser retornado corretamente")
  verify(mockDriver).quit() // Verifica se o WebDriver foi encerrado
 }
}