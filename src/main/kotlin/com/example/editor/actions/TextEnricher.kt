package com.example.editor.actions

import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component

@Component
open class TextEnricher(private val chatModel: ChatModel) {

    @Value("classpath:/prompts/check-paragraph.st")
    lateinit var enrichParagraphPrompt: Resource

    fun enrichText(text: String): String {
        if (text.isBlank()) {
            return text
        }

        val message = PromptTemplate(enrichParagraphPrompt).createMessage(mapOf("text" to text))
        val prompt = Prompt(message)
        val result = chatModel.call(prompt).results.map { it.output.content }.firstOrNull() ?: ""

        return result
    }
}