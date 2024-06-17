package com.infinum.academy.configuration

import com.infinum.academy.service.car.WebClientCarsService
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@Configuration
@EnableScheduling
class SchedulerConfiguration(val webClientCarsService: WebClientCarsService) {
    @Scheduled(fixedRate = 86400000)
    fun updateManufacturersAndModels() {
        webClientCarsService.updateManufacturersAndModels()
    }
}
