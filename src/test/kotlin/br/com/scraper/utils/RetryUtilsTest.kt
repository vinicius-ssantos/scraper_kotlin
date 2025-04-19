package br.com.scraper.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class RetryUtilsTest {

 @Test
 fun `deve executar operacao com sucesso na primeira tentativa`() {
  // Mock das dependências
  val delayStrategy = mock(DelayStrategy::class.java)
  val waitMechanism = mock(WaitMechanism::class.java)

  // Instância do RetryUtils
  val retryUtils = RetryUtils(delayStrategy, waitMechanism)

  // Operação de teste
  val result = retryUtils.executeWithRetry(operation = { "sucesso" })

  // Verificações
  assertEquals("sucesso", result)
  verifyNoInteractions(delayStrategy, waitMechanism)
 }

 @Test
 fun `deve realizar retries e falhar apos o maximo de tentativas`() {
  // Mock das dependências
  val delayStrategy = mock(DelayStrategy::class.java)
  val waitMechanism = mock(WaitMechanism::class.java)
  `when`(delayStrategy.calculateDelay(anyInt())).thenReturn(100L)

  // Instância do RetryUtils
  val retryUtils = RetryUtils(delayStrategy, waitMechanism)

  // Operação que sempre falha
  val exception = assertThrows(RuntimeException::class.java) {
   retryUtils.executeWithRetry(maxRetries = 3) {
    throw RuntimeException("Erro simulado")
   }
  }

  // Verificações
  assertEquals("Erro simulado", exception.message)
  verify(delayStrategy, times(3)).calculateDelay(anyInt())
  verify(waitMechanism, times(3)).waitFor(100L)
 }

 @Test
 fun `deve realizar retries e ter sucesso antes do maximo de tentativas`() {
  // Mock das dependências
  val delayStrategy = mock(DelayStrategy::class.java)
  val waitMechanism = mock(WaitMechanism::class.java)
  `when`(delayStrategy.calculateDelay(anyInt())).thenReturn(100L)

  // Instância do RetryUtils
  val retryUtils = RetryUtils(delayStrategy, waitMechanism)

  // Contador para simular falhas nas primeiras tentativas
  var attempt = 0
  val result = retryUtils.executeWithRetry(maxRetries = 3) {
   if (attempt++ < 2) throw RuntimeException("Erro simulado")
   "sucesso"
  }

  // Verificações
  assertEquals("sucesso", result)
  verify(delayStrategy, times(2)).calculateDelay(anyInt())
  verify(waitMechanism, times(2)).waitFor(100L)
 }
}