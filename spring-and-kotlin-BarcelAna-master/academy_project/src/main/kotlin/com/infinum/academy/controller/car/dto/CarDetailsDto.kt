package com.infinum.academy.controller.car.dto

import java.time.LocalDate
import java.time.Year
import java.util.*

data class CarDetailsDto(
    val id: UUID,
    val manufacturer: String,
    val model: String,
    val productionYear: Year,
    val addedAt: LocalDate,
    val vin: String,
    var isCheckUpNecessary: Boolean = false
) {
    constructor(car: CarDto, isCheckUpNecessary: Boolean) : this(
        car.id,
        car.manufacturer,
        car.model,
        car.productionYear,
        car.addedAt,
        car.vin,
        isCheckUpNecessary
    )
}
