package com.infinum.academy.service.car

import com.infinum.academy.exceptions.CarModelNotFoundException
import com.infinum.academy.repository.car.CarModelRepository
import nu.studer.sample.tables.pojos.CarModels
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class CacheService(val modelRepository: CarModelRepository) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Cacheable("model")
    fun getCarModel(manufacturer: String, model: String): CarModels {
        logger.info("Caching car model")

        try {
            return modelRepository.findByManufacturerNameAndModelName(manufacturer, model)
        } catch (_: NoSuchElementException) {
            throw CarModelNotFoundException("Car model with manufacturer: $manufacturer and model: $model does not exist")
        }
    }

    @CacheEvict("model")
    fun cleanCache() {
        logger.info("Cleaning cache")
    }
}
