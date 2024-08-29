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
class TextChecker(
    private val chatModel: ChatModel,
    private val contentCache: ContentCache,
) {

    @Value("classpath:/prompts/check-paragraph.st")
    lateinit var checkParagraphPrompt: Resource

    private val logger = LoggerFactory.getLogger(TextChecker::class.java)

    fun checkText(text: String): String {
        logger.info("Running TextChecker")
        if (text.isBlank()) {
            logger.info("Text is blank")
            return text
        }

        val paragraphs = text.split(Regex("\\n\\n"))
            .asSequence()
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .flatMapIndexed() { index, paragraph ->
                val cached = contentCache.exists(paragraph)
                if (cached) {
                    logger.info("Paragraph #${index+1} not changed")
                    listOf(paragraph)
                } else {
                    val promptTemplate = PromptTemplate(checkParagraphPrompt).createMessage(mapOf("text" to paragraph))
                    val prompt = Prompt(promptTemplate)
                    logger.info("Calling model with paragraph #${index+1}")
                    val chatResponse = chatModel.call(prompt)
                    chatResponse.results.map { it.output.content }.filter { it.isNotBlank() }
                }
            }
            .toList()

        contentCache.setAll(paragraphs)
        logger.info("TextChecker finished")
        return paragraphs.joinToString(separator = "\n\n")
    }
}