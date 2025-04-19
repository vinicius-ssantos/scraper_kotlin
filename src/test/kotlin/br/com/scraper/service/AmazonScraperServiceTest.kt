package br.com.scraper.service

import br.com.scraper.core.model.Product
import br.com.scraper.core.strategy.DelayStrategy
import br.com.scraper.core.strategy.WaitMechanism
import br.com.scraper.infrastructure.selenium.SeleniumSessionManager
import br.com.scraper.infrastructure.service.AmazonScraperService
import br.com.scraper.infrastructure.writer.JsonWriter
import br.com.scraper.utils.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import kotlin.test.assertEquals

class AmazonScraperServiceTest {

    private lateinit var sessionManager: SeleniumSessionManager
    private lateinit var delayStrategy: DelayStrategy
    private lateinit var waitMechanism: WaitMechanism
    private lateinit var jsonWriter: JsonWriter
    private lateinit var driver: WebDriver
    private lateinit var service: AmazonScraperService

    @BeforeEach
    fun setup() {
        sessionManager = mock()
        delayStrategy = mock()
        waitMechanism = mock()
        jsonWriter = mock()
        driver = mock()

        service = AmazonScraperService(jsonWriter, sessionManager, delayStrategy, waitMechanism)

        val mockSelectors = mapOf(
            "product_block" to listOf("div.produto", "div.fallback"),
            "title" to listOf(".titulo"),
            "price" to listOf(".preco"),
            "rating" to listOf(".avaliacao"),
            "reviews" to listOf(".comentarios")
        )
        service.javaClass.getDeclaredField("selectors").apply {
            isAccessible = true
            set(service, mockSelectors)
        }
    }

    @Test
    fun `deve retornar produto quando seletor principal encontrar elementos`() {
        val tituloElement = mock<WebElement> { on { text } doReturn "Produto" }
        val precoElement = mock<WebElement> { on { text } doReturn "R$ 10" }
        val avaliacaoElement = mock<WebElement> { on { text } doReturn "4.3" }
        val comentariosElement = mock<WebElement> { on { text } doReturn "25" }

        val element = mock<WebElement> {
            on { getAttribute("data-asin") } doReturn "ASIN-123"
            on { findElement(By.cssSelector(".titulo")) } doReturn tituloElement
            on { findElement(By.cssSelector(".preco")) } doReturn precoElement
            on { findElement(By.cssSelector(".avaliacao")) } doReturn avaliacaoElement
            on { findElement(By.cssSelector(".comentarios")) } doReturn comentariosElement
        }


        whenever(driver.findElements(By.cssSelector("div.produto"))).thenReturn(listOf(element))

        whenever(sessionManager.useSession<List<Product>>(any())).thenAnswer {
            it.getArgument<(WebDriver) -> List<Product>>(0).invoke(driver)
        }

        val produtos = service.startScraping("https://sem-campos.com")

        assertEquals(1, produtos.size)
        val produto = produtos[0]
        assertEquals("ASIN-123", produto.asin)
        assertEquals("Produto", produto.title)
        assertEquals("R$ 10", produto.price)
        assertEquals("4.3", produto.rating)
        assertEquals("25", produto.reviews)
    }



    @Test
    fun `deve utilizar fallback quando seletor principal nao retorna elementos`() {
        val fallbackTitulo = mock<WebElement> { on { text } doReturn "Fallback" }
        val fallbackPreco = mock<WebElement> { on { text } doReturn "R$ 99" }
        val fallbackAvaliacao = mock<WebElement> { on { text } doReturn "4.9" }
        val fallbackComentarios = mock<WebElement> { on { text } doReturn "150" }

        val fallbackElement = mock<WebElement> {
            on { getAttribute("data-asin") } doReturn "ASIN-FALLBACK"
            on { findElement(By.cssSelector(".titulo")) } doReturn fallbackTitulo
            on { findElement(By.cssSelector(".preco")) } doReturn fallbackPreco
            on { findElement(By.cssSelector(".avaliacao")) } doReturn fallbackAvaliacao
            on { findElement(By.cssSelector(".comentarios")) } doReturn fallbackComentarios
        }

        whenever(driver.findElements(By.cssSelector("div.produto"))).thenReturn(emptyList())
        whenever(driver.findElements(By.cssSelector("div.fallback"))).thenReturn(listOf(fallbackElement))

        whenever(sessionManager.useSession<List<Product>>(any())).thenAnswer {
            it.getArgument<(WebDriver) -> List<Product>>(0).invoke(driver)
        }

        val result = service.startScraping("https://fallback.com")
        assert(result.size == 1)
        assert(result[0].asin == "ASIN-FALLBACK")
        verify(jsonWriter).write(result)
    }

    @Test
    fun `deve preencher como Indisponivel quando elementos nao encontrados`() {
        val brokenElement = mock<WebElement> {
            on { getAttribute("data-asin") } doReturn null
            on { findElement(any()) } doThrow RuntimeException("Elemento não encontrado")
        }

        whenever(driver.findElements(any())).thenReturn(listOf(brokenElement))

        whenever(sessionManager.useSession<List<Product>>(any())).thenAnswer {
            it.getArgument<(WebDriver) -> List<Product>>(0).invoke(driver)
        }

        val result = service.startScraping("https://sem-campos.com")
        val produto = result.first()

        assert(produto.title == "Indisponível")
        assert(produto.price == "Indisponível")
        assert(produto.rating == "Indisponível")
        assert(produto.reviews == "Indisponível")
        assert(produto.asin == "Indisponível")
    }

    @Test
    fun `deve retornar lista vazia quando nenhum seletor encontrar elementos`() {
        whenever(driver.findElements(any())).thenReturn(emptyList())

        whenever(sessionManager.useSession<List<Product>>(any())).thenAnswer {
            it.getArgument<(WebDriver) -> List<Product>>(0).invoke(driver)
        }

        val resultado = service.startScraping("https://vazio.com")
        assert(resultado.isEmpty())
        verify(jsonWriter).write(emptyList())
    }
}
