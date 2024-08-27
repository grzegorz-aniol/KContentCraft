package com.example.editor

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.cdimascio.dotenv.dotenv
import java.util.concurrent.CompletableFuture

class TextChecker {

    private val client = HttpClient.newBuilder().build()
    private val objectMapper = jacksonObjectMapper()
    private val dotenv = dotenv()
    private val apiKey = dotenv["OPENAI_API_KEY"]
    private val modelName = dotenv["OPENAI_MODEL_NAME"]

    fun checkText(text: String, callback: (List<String>) -> Unit) {
        if (text.isBlank()) {
            callback(listOf("The text area is empty."))
            return
        }

        val promptTemplate = """Translate the following text to English and fix grammar or style mistakes.
                            Don't respond with any additional comments.
                            <begin>{text}<end>""".trimIndent()

        val messages = text.split(Regex("\\n\\n"))
            .map { it.trim() }
            .filter { it.isNotBlank() }

        val results = mutableListOf<String>()

        val requests = messages.map { message ->
            val prompt = promptTemplate.replace("{text}", message)
            val requestBody = objectMapper.writeValueAsString(
                mapOf(
                    "model" to modelName,
                    "messages" to listOf(mapOf("role" to "user", "content" to prompt)),
                    "temperature" to 0.7,
                    "max_tokens" to 1000
                )
            )
            HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer $apiKey")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build()
        }

        val futures = requests.map { request ->
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply { response ->
                    if (response.statusCode() == 200) {
                        val responseBody: Map<String, Any> = objectMapper.readValue(response.body())
                        val choices = responseBody["choices"] as? List<*>?
                        val result = choices?.mapNotNull {
                            val item = it as Map<String, *>?
                            val message = item?.get("message") as Map<String, String>?
                            message?.get("content")?.trim()
                        }
                        result?.firstOrNull() ?: "No response from the server."
                    } else {
                        "Error: ${response.statusCode()}"
                    }
                }
        }

        CompletableFuture.allOf(*futures.toTypedArray()).thenAccept {
            futures.forEach { future ->
                results.add(future.join())
            }
            callback(results)
        }
    }
}