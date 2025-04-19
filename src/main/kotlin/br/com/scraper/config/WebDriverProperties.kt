package br.com.scraper.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "webdriver")
class WebDriverProperties {
    lateinit var userAgents: List<String>
    lateinit var arguments: List<String>
}
