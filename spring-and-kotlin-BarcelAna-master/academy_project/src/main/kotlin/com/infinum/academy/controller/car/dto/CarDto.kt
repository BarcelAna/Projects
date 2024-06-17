package com.infinum.academy.controller.car.dto

import java.time.LocalDate
import java.time.Year
import java.util.*

data class CarDto(
    val manufacturer: String,
    val model: String,
    val productionYear: Year,
    val vin: String,
    var addedAt: LocalDate,
    var id: UUID
)
