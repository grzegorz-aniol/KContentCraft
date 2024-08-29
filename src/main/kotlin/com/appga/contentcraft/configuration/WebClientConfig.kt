package com.appga.contentcraft.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.reactive.function.client.WebClient

//@Configuration
class WebClientConfig {
    private val jsonUtf8 = " application/json; charset=utf-8"
    @Bean
    fun webClient(objectMapper: ObjectMapper): WebClient {
        return WebClient.builder()
            .codecs { configurer: ClientCodecConfigurer ->
                configurer.defaultCodecs()
                    .jackson2JsonEncoder(Jackson2JsonEncoder(objectMapper, APPLICATION_JSON_UTF8))
                configurer.defaultCodecs()
                    .jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper, APPLICATION_JSON_UTF8))
            }
            .build()
    }
}