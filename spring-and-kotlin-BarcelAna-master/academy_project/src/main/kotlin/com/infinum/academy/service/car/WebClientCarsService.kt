package com.infinum.academy.service.car

import com.infinum.academy.controller.car.dto.CarsApiResponse
import com.infinum.academy.exceptions.CarsApiException
import com.infinum.academy.repository.car.CarModelRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Service
class WebClientCarsService(
    val webClient: WebClient,
    @Value("\${cars-service.base-url}")val baseUrl: String,
    val modelRepository: CarModelRepository,
    val cacheService: CacheService
) {
    fun fetchManufacturersAndModels() =
        webClient
            .get()
            .uri(baseUrl)
            .retrieve()
            .bodyToMono<CarsApiResponse>()
            .map { carsResponse -> carsResponse.cars }
            .block()

    fun updateManufacturersAndModels() {
        val list = fetchManufacturersAndModels() ?: throw CarsApiException("Can't fetch cars from API")
        cacheService.cleanCache()
        for (car in list) {
            for (model in car.models) {
                try {
                    modelRepository.save(car.manufacturer, model)
                } catch (_: DuplicateKeyException) {}
            }
        }
    }
}
