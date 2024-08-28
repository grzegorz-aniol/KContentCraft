package com.example.editor.actions

import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component


@Component
open class TextChecker(private val chatModel: ChatModel) {

    @Value("classpath:/prompts/check-paragraph.st")
    lateinit var checkParagraphPrompt: Resource

    fun checkText(text: String): String {
        if (text.isBlank()) {
            return text
        }

        val paragraphs = text.split(Regex("\\n\\n"))
            .asSequence()
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .map { paragraph -> PromptTemplate(checkParagraphPrompt).createMessage(mapOf("text" to paragraph)) }
            .map { message -> Prompt(message) }
            .flatMap { prompt ->
                val chatResponse = chatModel.call(prompt)
                chatResponse.results.map { it.output.content }
            }.filter { it.isNotBlank() }
            .toList()

        return paragraphs.joinToString(separator = "\n\n")
    }
}