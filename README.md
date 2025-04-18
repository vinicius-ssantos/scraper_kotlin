
# 🕷️ KotlinScraper

![Kotlin](https://img.shields.io/badge/kotlin-1.9-blueviolet)
![Gradle](https://img.shields.io/badge/gradle-8.10-green)
![Selenium](https://img.shields.io/badge/selenium-4.19.1-lightgrey)
![Status](https://img.shields.io/badge/status-working-success)

Scraper modular desenvolvido em **Kotlin + Selenium**, com arquitetura limpa, scraping resiliente da **Amazon Brasil**, logging estruturado e saída em JSON. Projetado para ser extensível e escalável.

---

## 🚀 Tecnologias

- 🧠 **Kotlin (JVM)** — conciso e expressivo
- 🧪 **Selenium WebDriver** — automação com Chrome Headless
- ⚙️ **WebDriverManager** — gerencia o ChromeDriver dinamicamente
- 🧾 **SLF4J + Logback** — logging robusto e rotativo
- 📦 **Jackson** — serialização JSON
- 🌐 **Jsoup** — suporte a parsing híbrido (opcional)
- 🐘 **Gradle 8+**
- ☕ **JDK 17+**

---

## 📁 Estrutura do Projeto

```bash
src/main/kotlin/br/com/scraper/
├── ScraperApplication.kt         # Entrypoint da aplicação
├── config/                       # JSON writer, retry, logging
├── model/                        # Entidade Product
├── selector/                     # Loader de seletores externos
├── selenium/                     # WebDriverFactory e sessão
└── service/                      # AmazonScraperService
```

---

## 🔧 Como Buildar

```bash
./gradlew clean shadowJar
```

Saída gerada em:

```
build/libs/scraper-1.0-SNAPSHOT.jar
```

---

## ▶️ Como Executar

```bash
# via Gradle
./gradlew run

# ou via JAR
java -jar build/libs/scraper-1.0-SNAPSHOT.jar
```

---

## 📄 Output

- Produtos extraídos são salvos como:
  ```
  output/products_info_YYYY-MM-DD_HH-mm-ss.json
  ```
- Logs rotacionados são salvos em:
  ```
  logs/scraping-YYYY-MM-DD_HH-mm-ss.log
  ```

---

## 🔍 Seletor Dinâmico

Todos os seletores são carregados de:

```
src/main/resources/selectors_amazon.txt
```

Exemplo de conteúdo:

```txt
product_block=div[data-component-type='s-search-result']
title=h2 span.a-text-normal
price=span.a-price span.a-offscreen
rating=span.a-icon-alt
reviews=span[aria-label$=" avaliações"] span
```

---

## 🔄 Retry Inteligente

Falhas no scraping disparam automaticamente **3 tentativas com backoff exponencial**, garantindo resiliência contra problemas temporários.

---

## 🐳 Docker Ready

```bash
docker build -t scraper-kotlin .
docker run --rm -v $PWD/output:/app/output -v $PWD/logs:/app/logs scraper-kotlin
```

---

## 🔮 Roadmap

- [ ] 🔁 Scraping paralelo com múltiplos drivers
- [ ] 🧩 API REST com Spring Boot
- [ ] 📊 Monitoramento com Prometheus + Micrometer
- [ ] 🔀 Proxy rotation + header spoofing
- [ ] 💾 Persistência com Mongo/Postgres
