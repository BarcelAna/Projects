package com.infinum.academy.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class HttpConfiguration {
    @Bean
    fun carModelWebClient(
        webClientBuilder: WebClient.Builder,
        properties: CarModelWebClientProperties
    ): WebClient {
        return webClientBuilder.baseUrl(properties.baseUrl).build()
    }
}
