package com.infinum.academy.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ControllerExceptionHandler {
    @ExceptionHandler(value = [Exception::class])
    fun handleException(ex: Exception): ResponseEntity<String> {
        println("Something bad happened: ${ex.message}")
        return ResponseEntity("Error occurred", HttpStatus.BAD_REQUEST)
    }
}
