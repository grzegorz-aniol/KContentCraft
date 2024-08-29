package com.appga.contentcraft.actions

import com.appga.contentcraft.store.ContentCache
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component

@Component
class TextEnricher(
    private val chatModel: ChatModel,
    private val contentCache: ContentCache,
) {

    @Value("classpath:/prompts/check-paragraph.st")
    lateinit var enrichParagraphPrompt: Resource

    private val logger = LoggerFactory.getLogger(TextEnricher::class.java)

    fun enrichText(text: String): String {
        logger.info("Running TextEnricher")
        if (text.isBlank()) {
            logger.info("Text is blank")
            return text
        }

        val message = PromptTemplate(enrichParagraphPrompt).createMessage(mapOf("text" to text))
        val prompt = Prompt(message)
        val result = chatModel.call(prompt).results.map { it.output.content }.firstOrNull() ?: ""

        contentCache.setAll(result.split("\\n\\n").filter { it.isNotBlank() })

        logger.info("TextEnricher finished")
        return result
    }
}