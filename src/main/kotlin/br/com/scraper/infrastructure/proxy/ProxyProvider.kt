package br.com.scraper.infrastructure.proxy

import org.openqa.selenium.Proxy
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ProxyProvider(
    @Value("\${nordvpn.username}") private val username: String,
    @Value("\${nordvpn.password}") private val password: String,
    @Value("\${nordvpn.proxies}") private val proxies: List<String>
) {

    fun getRandomProxy(): Proxy {
        val proxyHost = proxies.random()
        val socksAddress = "$proxyHost:1080"

        return Proxy().apply {
            proxyType = Proxy.ProxyType.MANUAL
            socksProxy = socksAddress
            socksUsername = username
            socksPassword = password
        }
    }
}
