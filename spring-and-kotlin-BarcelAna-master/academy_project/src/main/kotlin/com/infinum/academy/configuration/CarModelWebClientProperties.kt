package com.infinum.academy.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "cars-service")
@ConstructorBinding
data class CarModelWebClientProperties(
    val baseUrl: String
)
