package com.infinum.academy.controller.checkup

import com.infinum.academy.service.checkup.CheckUpService
import org.springframework.http.CacheControl
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.time.Duration
import java.util.*

@RequestMapping("/checkup")
@Controller
class CheckUpController(
    val checkUpService: CheckUpService
) {

    @GetMapping("/analytics")
    fun getAnalytics() = ResponseEntity
        .ok()
        .cacheControl(CacheControl.maxAge(Duration.ofSeconds(10)))
        .contentType(MediaType.APPLICATION_JSON)
        .body(checkUpService.getAnalytics())
}
