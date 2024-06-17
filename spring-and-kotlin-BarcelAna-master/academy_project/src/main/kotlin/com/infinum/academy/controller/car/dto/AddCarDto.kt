package com.infinum.academy.controller.car.dto

import java.time.Year
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class AddCarDto(
    @field:NotBlank(message = "Car must have manufacturer")
    val manufacturer: String,
    @field:NotBlank(message = "Car must have model")
    val model: String,
    @field:NotNull(message = "Car must have production year")
    val productionYear: Year,
    @field:Size(min = 16, max = 16, message = "VIN must be 16 characters long")
    val vin: String
)
