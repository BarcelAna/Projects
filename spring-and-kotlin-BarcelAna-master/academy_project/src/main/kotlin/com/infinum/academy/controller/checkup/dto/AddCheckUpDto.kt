package com.infinum.academy.controller.checkup.dto

import org.jetbrains.annotations.NotNull
import javax.validation.constraints.NotBlank

data class AddCheckUpDto(
    @field:NotBlank(message = "Check up must have worker name")
    val workerName: String,
    @field:NotNull("Check up must have price")
    val price: Long
)
