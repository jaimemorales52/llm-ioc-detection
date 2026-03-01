# LLM IoC Detection – Spring Boot Backend

This repository contains the Spring Boot backend used in the experiments of the paper *"Can LLMs Identify Secrets Embedded in Code?"*.

The goal of this project is to evaluate how different Large Language Models (LLMs) handle **Indicators of Compromise (IoCs)** embedded in source code.  
In our experiments, IoCs are encoded as *secrets* inside JavaScript files (e.g., IP addresses) that are first **obfuscated and/or encrypted** and then analysed through LLM queries.

The backend prepares this process by:
- taking JavaScript code and embedding an IoC (in this implementation, an IP address),
- applying a configurable pipeline of obfuscation and encryption transformations to that IP,
- and finally sending the transformed code to several LLM providers to assess whether they can still detect and recover the hidden secret.

It exposes REST endpoints to:
- send code samples to several LLM providers,
- obtain a normalized decision about IoC presence (`YES` / `NO` / `DON'T_KNOW`),
- retrieve the *secret value* (e.g., the IP) when the model claims to have found it.

> ⚠️ This project is for research and educational purposes only.  
> Do not use it on sensitive or proprietary code in production.

---

## 1. Project structure

Base package: `com.phd.llm`

```text
com.phd.llm
 ├── client/              # HTTP clients for each LLM provider
 ├── config/              # Spring configuration (Mongo, etc.)
 ├── controller/          # REST controllers per provider + file handling
 ├── dao/                 # (currently unused / placeholder for future persistence)
 ├── model/               # Parsed / normalized responses per provider
 │   ├── ai21/
 │   ├── cohere/
 │   ├── deepseek/
 │   ├── gemini/
 │   ├── grok/
 │   ├── openai/
 │   └── others/
 ├── properties/          # Configuration properties (API keys, URLs, models, etc.)
 ├── service/             # Service interfaces
 │   └── impl/            # Service implementations (IoC detection logic)
 └── LlmApplication.java  # Spring Boot entry point
```

### 1.1. `client`

`com.phd.llm.client`

- `AnthropicClient.java`
- `DeepSeekClient.java`
- `GeminiClient.java`
- `GrokClient.java`
- `OpenAIClient.java`

Each client encapsulates the **HTTP call** to a specific LLM provider using `HttpClient`.  
It takes a prompt (containing the obfuscated code and the IoC detection instruction) and returns the raw response.  
All higher‑level logic (what constitutes an IoC, how to interpret `YES/NO/DON'T_KNOW`, how to extract the secret) lives in the service layer.

### 1.2. `config`

`com.phd.llm.config`

- `MongoConfig.java`: Spring configuration for MongoDB.  
  Currently not required for the basic IoC detection flow, but available if you want to persist requests/responses in a database.

### 1.3. `controller`

`com.phd.llm.controller`

- `AI21Controller.java`
- `AnthropicController.java`
- `CohereController.java`
- `DeepSeekController.java`
- `FileController.java`
- `GeminiController.java`
- `GrokController.java`
- `OpenAIController.java`

Controllers expose REST endpoints that accept source code (single files or directories) and delegate to the corresponding service:

- Receive JavaScript code where IoCs (secrets) may be embedded and obfuscated.
- Call the appropriate `*ServiceImpl` to run IoC detection via an LLM.
- Return a **normalized IoC detection result**, including:
  - whether an IoC is present (`YES` / `NO` / `DON'T_KNOW`),
  - the recovered secret value (e.g., IP address) when available.

### 1.4. `dao`

`com.phd.llm.dao`

- Currently unused.  
  Reserved for future DAO/repository classes if you decide to move from file‑based storage to a database for IoC detections and LLM responses.

### 1.5. `model`

`com.phd.llm.model.*`

- `com.phd.llm.model.ai21`
- `com.phd.llm.model.cohere`
- `com.phd.llm.model.deepseek`
- `com.phd.llm.model.gemini`
- `com.phd.llm.model.grok`
- `com.phd.llm.model.openai`
- `com.phd.llm.model.others`

These packages define the **parsed / normalized responses** from each provider.  
They map provider‑specific JSON to a common internal representation of IoC detection, for example:

- `decision`: `YES` / `NO` / `DON'T_KNOW`
- `iocType`: `"IP"` (can be extended to domains, hashes, etc.)
- `iocValue`: the recovered secret (e.g., `"192.168.17.65"`)
- `rawText`: raw provider response (for debugging)

### 1.6. `service` and `service.impl`

`com.phd.llm.service` and `com.phd.llm.service.impl`

Main implementations include:

- `AI21ServiceImpl.java`
- `AnthropicServiceImpl.java`
- `CohereServiceImpl.java`
- `DeepSeekServiceImpl.java`
- `DirectoryServiceImpl.java`
- `FileServiceImpl.java`
- `GeminiServiceImpl.java`
- `GrokServiceImpl.java`
- `OpenAIServiceImpl.java`
- `TestGetProperties.java` (helper to validate configuration)

Each service encapsulates the **IoC detection logic**:

- Builds the IoC detection prompt given a piece of code:
  - explains to the model that the task is to look for a security‑relevant artifact (IoC),
  - restricts the answer to `YES`, `NO`, or `DON'T_KNOW`,
  - asks for the secret value (IP address) if `YES`.
- Calls the corresponding `*Client`.
- Parses and normalizes the provider response into the `model` classes.

`FileServiceImpl` / `DirectoryServiceImpl` add file‑system support (read JS files, send them to a provider and aggregate IoC detections).

### 1.7. `LlmApplication.java`

- `com.phd.llm.LlmApplication`
- Standard `@SpringBootApplication` entry point.

---

## 2. How the backend works

At a high level, this backend is designed to reproduce the experimental pipeline described in our research:  
embed an IoC (in this implementation, an IP address) into JavaScript code, apply a sequence of obfuscation and encryption transformations, and finally test whether different LLMs can still detect and recover that hidden secret through an API call.

### 2.1. File based workflow (`FileController`)

The `FileController` provides the most convenient way to run the full process over real code.  
It exposes endpoints that accept either a single JavaScript file or a directory containing multiple files:

- **Single file mode** – upload or point to one JS file to run the complete pipeline on that sample.
- **Directory mode** – point to a folder of JS files to process a batch in one call.

Conceptually, when you trigger these endpoints the backend performs the following steps:

1. **IoC embedding**  
   The system takes each JavaScript file and embeds an IoC (here, an IP address) as a secret in the code.  
   The exact location and representation of the IP are controlled so that we keep a ground‑truth value for later evaluation.

2. **Obfuscation and encryption phases**  
   The embedded IP is then transformed through a series of obfuscation/encryption phases, mirroring the phases used in our paper  
   (e.g., simple syntactic changes, structural obfuscation, XOR encryption, AES‑256 encryption, and combinations thereof).  
   Each phase produces a new variant of the original file where the IoC becomes progressively harder to spot.

3. **LLM querying**  
   For every transformed variant, the backend builds a standardized IoC‑detection prompt and sends it to the configured LLMs.  
   The models are asked to decide whether an IoC is present (`YES` / `NO` / `DON'T_KNOW`) and, if so, to return the recovered IP address.

4. **Normalization and result collection**  
   The raw responses from the providers are parsed into a common internal format, so that all models can be compared consistently.  
   This normalized output can then be exported or post‑processed to compute detection and hallucination metrics, as done in the paper.

Although the paper is not yet publicly available, this file‑based workflow is designed to match the experimental setup described there, allowing other researchers to replay or extend the same kind of analysis.

### 2.2. Provider specific controllers (API only access to LLMs)

In addition to the `FileController`, the project exposes one controller per LLM provider:

- `OpenAIController`
- `GeminiController`
- `AnthropicController`
- `GrokController`
- `CohereController`
- `AI21Controller`
- `DeepSeekController`

These controllers are intentionally minimal: they provide **API style access** to the underlying models, without going through a chat UI.  
Instead of interacting with the providers via their web consoles, you can:

- send a piece of (possibly obfuscated) JavaScript code directly to the backend,
- let the backend construct the IoC‑detection prompt,
- and receive a normalized JSON response with:
  - the provider name,
  - the decision (`YES` / `NO` / `DON'T_KNOW`),
  - and the recovered secret (IP) when applicable.

This design makes it easy to script large experiments, integrate IoC detection into other tools, or swap models/providers while keeping the same analysis logic on your side.

---

## 3. Configuration (`resources`)

LLM API keys are stored in a separate file under ```src/main/resources/keys.properties```.
The repository uses placeholder values so that no real secrets are committed:

```
gemini.password=CHANGEMEAPIKEY
chatgpt.password=CHANGEMEAPIKEY
cohere.password=CHANGEMEAPIKEY
cohere.test.password=CHANGEMEAPIKEY
anthropic.password=CHANGEMEAPIKEY
gkrok.password=CHANGEMEAPIKEY
```

The specific model used for each provider is configured in the corresponding client class. By default, the backend is set up to call GPT‑4 Turbo (OpenAI, 2024, 128k), Gemini 1.5 Pro (Google, 2025, 1M), Claude 3 Sonnet (Anthropic, 2024, 200k), Grok‑2 (xAI, 2024, 128k), and Command‑R7B (Cohere, 2024, 128k).


---

## 4. How to start the project

### 4.1. Prerequisites

- Java 21 (or the version configured in your `pom.xml`)
- Maven
- Valid API keys for the LLM providers you want to use (OpenAI, Anthropic, Gemini, etc.)

### 4.2. Clone and configure

```bash
git clone https://github.com/jaimemorales52/llm-ioc-detection.git
cd llm-ioc-detection
```

Edit `src/main/resources/keys.properties` and replace the `CHANGE_ME_...` values with your real API keys.

### 4.3. Build

```bash
mvn clean package
```

### 4.4. Run

Using the packaged JAR:

```bash
java -jar target/llm-application-0.0.1-SNAPSHOT.jar
```

Or directly with Spring Boot:

```bash
mvn spring-boot:run
```

By default the application will start at:

```text
http://localhost:8080
```

---

## 5. Typical API usage

To run IoC detection you only need to provide a single JavaScript file or Folder: the backend takes care of embedding the IP, applying the obfuscation/encryption phases, and querying the LLMs.

---

## 6. Results format
For each processed file and transformation phase, the backend produces a row in a results table (e.g., exported as CSV/XLSX).
Each row has the following fields:

Phase: name of the transformation applied to the original file (e.g. flag, base64, obfus, superobfus, anonymize, decryptedXOR, etc.).

Contains IP: whether the model reports that the code contains an IP.

Extracted IP: the concrete IP string returned by the model (empty when no IP is reported).

Is Target IP: boolean flag indicating if the extracted IP matches the ground‑truth IP embedded for that phase.

LLM: provider used for that particular call (e.g. Anthropic, ChatGPT, Gemini, Grok).

File: name of the JavaScript file / variant.

Example snippet of the results table, showing a few phases for one provider:

Example snippet of the results table, showing a few phases for one provider:

| Phase        | Contains IP | Extracted IP    | Is Target IP | LLM  | File                         |
|-------------|-------------|-----------------|--------------|------|------------------------------|
| flag        | YES         | 192.168.17.65   | true         | Grok | flagBinaryConvert.js         |
| base64      | YES         | 192.168.17.65   | true         | Grok | base64BinaryConvert.js       |
| obfus       | YES         | 192.168.17.65   | true         | Grok | obfusBinaryConvert.js        |
| superobfus  | YES         | 192.168.17.65   | true         | Grok | superobfusBinaryConvert.js   |
| anonymize   | YES         | 192.168.17.65   | true         | Grok | anonymizeBinaryConvert.js    |
| decryptedXOR| NO          |                 | false        | Grok | decryptedXORBinaryConvert.js |


This format makes it straightforward to see, for each model and phase, whether it correctly finds the embedded IP, misses it, or hallucinates a different address.


## 7. Extending IoC types

Although the experiments in the paper focus on **IPs as secrets**, the architecture is prepared for other IoC types:

- domains,
- URLs,
- file hashes,
- API keys, etc.

To extend:

1. Update your models (`iocType`, `iocValue`) to support new kinds of secrets.
2. Adapt the prompts in the services to specify which IoC/secret type is being searched.
3. Adjust parsing of the returned value if the provider changes the format.

---

## 8. License

This project is licensed under the **MIT License**.  
See the `LICENSE` file in this repository for the full text.
