# 📘 **FiadoPay Simulator (Spring Boot + H2)**

Gateway de pagamento **FiadoPay** para a disciplina de Arquitetura e Virtualização da Informação / POOA.
Simula PSPs reais usando backend em memória (H2) e arquitetura baseada em **plugins + reflexão + processamento assíncrono**.

---

# 🚀 Como Rodar

### **Requisitos**

* JDK 21 ou JDK 22
* Maven 3.8+

### **Rodando via Maven**

```bash
./mvnw spring-boot:run
# ou
mvn spring-boot:run
```

### **Executando o JAR**

```bash
mvn clean package -DskipTests=false
java -jar target/fiadopay-sim-1.0.0.jar
```

### **Interfaces úteis**

* H2 console: [http://localhost:8080/h2](http://localhost:8080/h2)
* Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

# 🔄 Fluxo de Uso da API

### **1) Cadastrar Merchant**

```bash
curl -X POST http://localhost:8080/fiadopay/admin/merchants ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"MinhaLoja ADS\",\"webhookUrl\":\"http://localhost:8081/webhooks/payments\"}"
```

### **2) Obter Token**

```bash
curl -X POST http://localhost:8080/fiadopay/auth/token ^
  -H "Content-Type: application/json" ^
  -d "{\"client_id\":\"<clientId>\",\"client_secret\":\"<clientSecret>\"}"
```

### **3) Criar Pagamento**

```bash
curl -X POST http://localhost:8080/fiadopay/gateway/payments ^
  -H "Authorization: Bearer FAKE-<merchantId>" ^
  -H "Idempotency-Key: 550e8400-e29b-41d4-a716-446655440000" ^
  -H "Content-Type: application/json" ^
  -d "{\"method\":\"PIX\",\"currency\":\"BRL\",\"amount\":250.50,\"installments\":12,\"metadataOrderId\":\"ORD-456\"}"

```

### **4) Consultar Pagamento**

```bash
curl http://localhost:8080/fiadopay/gateway/payments/<paymentId>
```

---

# 🧩 Arquitetura e Decisões

* Sistema orientado a **plugins via anotações**
* Descoberta automática via **ReflectionRegistrar**
* Alta extensibilidade: novos métodos de pagamento, regras antifraude, webhooks e jobs podem ser adicionados sem tocar no core
* Processamento assíncrono com executor dedicado
* Idempotência completa incluindo cache de resposta
* Webhooks paralelos com retries

---

# 🏷️ Anotações Implementadas

* `@PaymentMethod`
* `@PaymentProcessor`
* `@AntiFraud`
* `@WebhookSink`
* `@ScheduledJob`

Cada anotação ativa um **registry**, permitindo carregamento dinâmico.

---

# 🔍 Mecanismo de Reflexão

O `ReflectionRegistrar` escaneia o classpath em runtime:

* encontra classes anotadas
* registra automaticamente
* conecta processadores, antifraude, webhooks e jobs
  → o sistema inteiro funciona **sem if/else**, totalmente modular.

---

# 🧵 Threads e Concorrência

* `PaymentExecutorService` processa pagamentos em threads separadas
* Webhooks são enviados de forma paralela
* Idempotência evita duplicações em chamadas concorrentes
* Antifraude roda antes do processamento

---

# 🧪 Testes Incluídos

* Testes de repositórios (H2)
* Testes do antifraude (HighAmountRule, Blacklist)
* Testes do PixProcessor usando anotação
* Testes da pipeline completa de pagamento
* Testes de idempotência (salva + retorna resposta)
* Testes de integração (Spring Boot)

---

# 👥 Função de Cada Pessoa

### **Fausto Alves — Plugins, Anotações e Reflection Registrar**

* Criou todas as anotações do sistema
* Implementou interfaces padrão
* Construção completa do `ReflectionRegistrar`
* Arquitetura base orientada a plugins

### **Amanda Oliveira — Registries de Processadores, Antifraude, Webhooks e Jobs**

* Criou o pacote `annotations.registry`
* Implementou 4 registries dinâmicos
* Integração direta com o ReflectionRegistrar
* Base para carregamento dinâmico do sistema

### **Matheus Costa — Motor de Antifraude**

* Criou `AntiFraudService`
* Implementou regras de antifraude
* Integração completa com registries

### **Sarah Ramos — Sistema de Webhooks**

* Criou `WebhookExecutorService`
* Handlers dinâmicos via anotação
* Execução paralela e logs

### **Andrey Gabriel — Testes Automatizados**

* Testes unitários e integração
* Testes de antifraude, pipeline e idempotência
* Garantiu cobertura das regras críticas

---

# ✔️ Checklist de Entrega

* [x] Repositório público
* [x] Código completo + pom.xml
* [x] README detalhado
* [x] Evidências
* [x] Testes implementados
* [x] Vídeo (faltando anexar)

---