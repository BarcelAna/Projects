package com.infinum.academy.controller.car.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class CarsApiResponse(
    @JsonProperty("cars") val cars: List<ManufacturerAndModels>
)

data class ManufacturerAndModels(
    @JsonProperty("manufacturer") val manufacturer: String,
    @JsonProperty("models") val models: List<String>
)
