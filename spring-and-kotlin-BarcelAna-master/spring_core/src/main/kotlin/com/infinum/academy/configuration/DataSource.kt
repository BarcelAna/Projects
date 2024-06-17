package com.infinum.academy.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class DataSource(
    @Value("\${datasource.dbName}")
    val dbName: String,
    @Value("\${datasource.username}")
    val username: String,
    @Value("\${datasource.password}")
    val password: String
)
