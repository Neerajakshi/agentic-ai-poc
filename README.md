# Agentic AI OpenAI POC

A deliberately small Spring Boot + Spring AI + OpenAI application for learning tool calling and the agent loop. The business data is fake and stored in Java code, so there is no database to configure.

## Prerequisites

- Java 17+
- Maven 3.6.3+
- An OpenAI API key

## 1. Set your OpenAI API key

Do **not** put the key directly in `application.properties` or commit it to GitHub. Spring AI reads it from the `OPENAI_API_KEY` environment variable.

### Windows PowerShell

For the current PowerShell session:

```powershell
$env:OPENAI_API_KEY="your-api-key-here"
```

Check that it is set without printing the key:

```powershell
if ($env:OPENAI_API_KEY) { "OPENAI_API_KEY is set" } else { "OPENAI_API_KEY is NOT set" }
```

For a persistent Windows user environment variable, you can use:

```powershell
[Environment]::SetEnvironmentVariable("OPENAI_API_KEY", "your-api-key-here", "User")
```

Restart your terminal/IDE after setting a persistent variable so the new environment is picked up.

## 2. Run the application

From the project directory:

```bash
mvn spring-boot:run
```

On Windows PowerShell the same command works:

```powershell
mvn spring-boot:run
```

## 3. Call the agent

Open this URL in your browser:

```text
http://localhost:8080/ask?question=Check%20order%201001,%20find%20out%20why%20it%20has%20a%20problem,%20and%20raise%20a%20support%20ticket%20if%20needed.
```

Or use curl:

```bash
curl "http://localhost:8080/ask?question=Check%20order%201001,%20find%20out%20why%20it%20has%20a%20problem,%20and%20raise%20a%20support%20ticket%20if%20needed."
```

## 4. Watch the console

You should see tool execution messages such as:

```text
[TOOL EXECUTED] getOrderStatus(1001)
[TOOL EXECUTED] getPaymentStatus(1001)
[TOOL EXECUTED] createSupportTicket(issue="...")
```

The exact order can vary because the model decides which tool to call next. The important part is that the model requests a tool and Spring AI executes the Java method.

## 5. Try a second order

```text
http://localhost:8080/ask?question=Check%20order%201002.%20Is%20there%20a%20problem%3F
```

This is useful because order 1002 is shipped and paid, so the agent normally should not create a ticket.

## What to observe

```text
User
  -> Spring Boot
  -> OpenAI model
  -> tool call request
  -> Java @Tool method
  -> tool result
  -> OpenAI model
  -> another tool call OR final answer
```

That repeated cycle is the part to understand before moving to RAG, memory, workflows, and multi-agent systems.

## Project files to study first

Start with only these three files:

```text
src/main/resources/application.properties
src/main/java/com/example/agentpoc/AgentController.java
src/main/java/com/example/agentpoc/OrderTools.java
```

In particular, study the `@Tool` methods in `OrderTools.java` and this call in `AgentController.java`:

```java
.tools(orderTools)
```

That is the bridge between your Java application and the model's tool-calling capability.

## Important

The POC uses fake in-memory data. No real database, payment service, or ticket system is contacted.

API usage is through your OpenAI project, so requests may consume API credits or incur API charges according to your account/project billing setup.
