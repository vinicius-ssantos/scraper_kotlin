package br.com.scraper.infrastructure.cookies

import org.openqa.selenium.Cookie
import org.openqa.selenium.WebDriver
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

object CookieManager {

    fun saveCookies(driver: WebDriver, file: File) {
        ObjectOutputStream(file.outputStream()).use { out ->
            out.writeObject(driver.manage().cookies)
        }
    }

    fun loadCookies(driver: WebDriver, file: File) {
        if (!file.exists()) return
        val cookies = ObjectInputStream(file.inputStream()).use { it.readObject() } as Set<Cookie>
        cookies.forEach { driver.manage().addCookie(it) }
    }
}
