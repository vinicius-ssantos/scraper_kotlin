plugins {
    id("org.jetbrains.kotlin.jvm") version "2.0.21"
    id("org.jetbrains.kotlin.kapt") version "2.0.21"
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    application
//    id("com.github.johnrengelman.shadow") version "8.1.1" // ADICIONAR

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
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }

    // Spring Boot Web
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.yaml:snakeyaml:2.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.17.0")

    implementation("org.springframework.boot:spring-boot-configuration-processor")
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.mockito:mockito-core:5.7.0")
//    testImplementation("org.mockito:mockito-inline:5.7.0")
}

application {
    mainClass.set("br.com.scraper.ScraperApiApplicationKt")
}




tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = true
}

tasks.named<Jar>("jar") {
    enabled = false
}
tasks.test {
    useJUnitPlatform()
    systemProperty("spring.profiles.active", "test")
    jvmArgs("-Djdk.instrument.traceUsage")
//    jvmArgs = (jvmArgs ?: mutableListOf()) + "-XX:+EnableDynamicAgentLoading"
}