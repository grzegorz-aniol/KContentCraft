package com.example.editor.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import java.nio.charset.StandardCharsets
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate


@Configuration
open class RestTemplateConfig {

    @Bean
    open fun restTemplate(objectMapper: ObjectMapper): RestTemplate {
        val restTemplate = RestTemplate()
        val converter = MappingJackson2HttpMessageConverter(objectMapper)
        converter.defaultCharset = StandardCharsets.UTF_8
        restTemplate.messageConverters = listOf<HttpMessageConverter<*>>(converter)
        return restTemplate
    }

}