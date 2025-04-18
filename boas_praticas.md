## 🧠 Boas Práticas para Projeto de Web Scraping/Crawler em **Java ou Kotlin** com Spring Boot + Selenium
> _Nota: este projeto será desenvolvido em Kotlin, mas as diretrizes são válidas também para Java._

Este documento define as diretrizes de qualidade para todo o código gerado ou mantido no projeto. O objetivo é garantir que o sistema seja limpo, modular, seguro, testável e extensível. Nenhum exemplo prático deve ser incluído diretamente neste documento.

---

### ✅ 1. Qualidade de Código (Clean Code)

- Nomes de variáveis, métodos e classes devem ser claros, descritivos e consistentes.
- Métodos/funções devem ser curtos e com responsabilidade única.
- Eliminar repetições de lógica (princípio DRY).
- Preferir composição a herança.
- Usar early return para reduzir aninhamento de código.
- Evitar uso de `null`; utilizar `Optional` (Java) ou `nullable` com `safe calls` e `elvis` (Kotlin).
- Não utilizar `System.out.println`; utilizar loggers.

---

### ✅ 2. Boas Práticas com Spring

- Usar injeção de dependência por construtor.
- Utilizar corretamente as anotações: `@Service`, `@Repository`, `@Component`, `@Configuration`.
- Utilizar `@Value` para propriedades externas.
- Organizar o projeto em pacotes/módulos com responsabilidade clara.

---

### ✅ 3. Segurança e Robustez

- Validar todas as entradas externas.
- Implementar fallback de seletores.
- Evitar valores hardcoded.
- Tratar exceções específicas com logs detalhados.

---

### ✅ 4. Scraping Inteligente

- Usar `WebDriverWait` para sincronização de elementos.
- Alternar `User-Agent` dinamicamente.
- Aplicar delays aleatórios.
- Persistir cookies para sessões futuras.
- Utilizar arquivos externos para seletores com fallback.

---

### ✅ 5. Retry e Tolerância a Falhas

- Toda operação crítica deve utilizar retry.
- Aplicar `exponential backoff` em falhas.
- Registrar detalhes de cada tentativa.
- Utilizar `RetryHandler`, `RetryTemplate` ou mecanismo equivalente.

---

### ✅ 6. Logging

- Utilizar SLF4J com `LoggerFactory`.
- Incluir nos logs: timestamp, correlation-id, URL, tipo de operação.
- Salvar logs em arquivos por data.

---

### ✅ 7. Desacoplamento e Extensibilidade

- Não acoplar seletores e URLs no código.
- Utilizar arquivos externos ou configuração.
- Arquitetura deve permitir novos scrapers e estratégias.

---

### ✅ 8. Persistência e Validação

- Validar os dados antes de persistir.
- Permitir diferentes formas de persistência.
- Separar lógica de scraping da persistência.

---

### ✅ 9. Escalabilidade e Paralelismo

- Utilizar `ExecutorService` (Java) ou `coroutines + Dispatcher` (Kotlin) com pool configurável.
- Usar concorrência controlada.
- Manter logs por thread ou tarefa.

---

### ✅ 10. Testabilidade

- Todo código deve ser testável.
- Usar mocks para dependências externas.
- Separar scraping da exposição REST.
- Utilizar JUnit (5) e Mockito (ou MockK para Kotlin) para testes unitários.

---

### ✅ 11. Princípios SOLID

#### S – Single Responsibility Principle (SRP)
- Cada classe deve ter uma única responsabilidade.
- Um componente deve ter apenas uma razão para mudar.

#### O – Open/Closed Principle (OCP)
- O código deve ser aberto para extensão, fechado para modificação.
- Usar abstrações para permitir novas funcionalidades sem alterar o core.

#### L – Liskov Substitution Principle (LSP)
- Subclasses e implementações devem manter o comportamento esperado das abstrações.
- Nenhuma implementação deve quebrar a substituibilidade.

#### I – Interface Segregation Principle (ISP)
- Interfaces devem ser pequenas e coesas.
- Nenhuma implementação deve depender de métodos que não usa.

#### D – Dependency Inversion Principle (DIP)
- Sempre depender de interfaces, não de implementações concretas.
- As dependências devem ser injetadas via construtor.

---

### ✅ 12. Boas Práticas com Kotlin

- Utilizar `extension functions` para reutilizar lógica comum.
- Preferir `data classes` para modelar entidades imutáveis.
- Usar `sealed classes` para representar estados e falhas de scraping.
- Utilizar `destructuring declarations` quando apropriado.
- Evitar `!!` (non-null assertions); preferir `?.`, `let`, `run`, `also`, `takeIf`.
- Evitar `companion object` excessivo; preferir injeção de dependência.

---

### ✅ 13. Observabilidade e Monitoramento

- Adicionar métricas personalizadas (ex: Micrometer, Prometheus).
- Criar painel de observabilidade com status por domínio ou tarefa.
- Emitir eventos de scraping via log estruturado ou filas (ex: Kafka, RabbitMQ).

---

### ✅ 14. Gerenciamento de Sessão e Detecção de Bloqueios

- Detectar páginas de bloqueio/CAPTCHA.
- Implementar reconhecimento de padrão HTML para identificar mudanças de layout.
- Rotacionar proxies e IPs dinamicamente.
- Criar rotina de teste de sessão automatizada.

---

### ✅ 15. Resiliência por Site (Scraping Contextual)

- Criar camada de abstração por domínio (ex: AmazonScraper, MercadoLivreScraper).
- Permitir overrides de lógica de parsing por domínio ou categoria.
- Utilizar feature flags para ativar/desativar scrapers individualmente.

---

### ✅ 16. Organização por Módulo

- Separar o projeto em módulos independentes, como:
    - `scraper-core`: interfaces e utilitários
    - `scraper-domain`: entidades e lógica de negócio
    - `scraper-adapters`: implementações Selenium, banco, etc.
    - `scraper-api`: REST, agendamento, autenticação

---

Essas regras devem ser seguidas rigorosamente em todo o projeto.

