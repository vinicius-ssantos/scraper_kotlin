package br.com.scraper.config

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
open class WebDriverPropertiesIntegrationTest {

    @Autowired
    private lateinit var webDriverProperties: WebDriverProperties

    @Test
    fun `deve carregar propriedades do arquivo application-test yml`() {
        assertEquals(2, webDriverProperties.userAgents.size)
        assertEquals("--headless", webDriverProperties.arguments[0])
    }
}