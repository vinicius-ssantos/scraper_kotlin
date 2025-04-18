plugins {
    kotlin("jvm") version "2.0.21"
    application
    id("com.github.johnrengelman.shadow") version "8.1.1" // ADICIONAR

}

group = "br.com.scraper"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation(kotlin("stdlib"))

    // Selenium
    implementation("org.seleniumhq.selenium:selenium-java:4.19.1")

    // WebDriverManager
    implementation("io.github.bonigarcia:webdrivermanager:5.7.0")

    // Logging
    implementation("org.slf4j:slf4j-api:2.0.12")
    implementation("ch.qos.logback:logback-classic:1.4.14")

    // JSON
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")

    // Jsoup (BeautifulSoup do Java/Kotlin)
    implementation("org.jsoup:jsoup:1.17.2")

    // Testes
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

application {
    mainClass.set("br.com.scraper.ScraperApplicationKt")
}


tasks {
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        archiveBaseName.set("scraper")
        archiveClassifier.set("")
        archiveVersion.set("1.0-SNAPSHOT")
    }
}


tasks.test {
    useJUnitPlatform()
}