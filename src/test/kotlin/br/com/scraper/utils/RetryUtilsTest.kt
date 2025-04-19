package br.com.scraper.utils

import br.com.scraper.core.strategy.DelayStrategy
import br.com.scraper.core.strategy.WaitMechanism
import br.com.scraper.infrastructure.strategy.DefaultRetryStrategy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class RetryUtilsTest {

 @Test
 fun `deve executar operacao com sucesso na primeira tentativa`() {
  val delayStrategy = mock(DelayStrategy::class.java)
  val waitMechanism = mock(WaitMechanism::class.java)

  val retryStrategy = DefaultRetryStrategy(delayStrategy, waitMechanism)

  val result = retryStrategy.execute(operation = { "sucesso" })

  assertEquals("sucesso", result)
  verifyNoInteractions(delayStrategy, waitMechanism)
 }

 @Test
 fun `deve realizar retries e falhar apos o maximo de tentativas`() {
  val delayStrategy = mock(DelayStrategy::class.java)
  val waitMechanism = mock(WaitMechanism::class.java)
  `when`(delayStrategy.calculateDelay(anyInt())).thenReturn(100L)

  val retryStrategy = DefaultRetryStrategy(delayStrategy, waitMechanism)

  val exception = assertThrows(RuntimeException::class.java) {
   retryStrategy.execute(3) {
    throw RuntimeException("Erro simulado")
   }
  }

  assertEquals("Erro simulado", exception.message)
  verify(delayStrategy, times(2)).calculateDelay(anyInt()) // 2 retries antes de falhar
  verify(waitMechanism, times(2)).waitFor(100L)
 }


 @Test
 fun `deve realizar retries e ter sucesso antes do maximo de tentativas`() {
  val delayStrategy = mock(DelayStrategy::class.java)
  val waitMechanism = mock(WaitMechanism::class.java)
  `when`(delayStrategy.calculateDelay(anyInt())).thenReturn(100L)

  val retryStrategy = DefaultRetryStrategy(delayStrategy, waitMechanism)

  var attempt = 0
  val result = retryStrategy.execute(3) {
   if (attempt++ < 2) throw RuntimeException("Erro simulado")
   "sucesso"
  }

  assertEquals("sucesso", result)
  verify(delayStrategy, times(2)).calculateDelay(anyInt())
  verify(waitMechanism, times(2)).waitFor(100L)
 }
}