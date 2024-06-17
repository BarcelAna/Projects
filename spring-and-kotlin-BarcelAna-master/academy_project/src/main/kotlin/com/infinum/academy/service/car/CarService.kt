package com.infinum.academy.service.car

import com.infinum.academy.controller.car.dto.AddCarDto
import com.infinum.academy.controller.car.dto.CarDetailsDto
import com.infinum.academy.repository.car.CarRepository
import com.infinum.academy.service.checkup.CheckUpService
import nu.studer.sample.tables.pojos.Cars
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CarService(
    val carRepository: CarRepository,
    val cacheService: CacheService,
    val checkUpService: CheckUpService
) {

    @Transactional
    fun addCar(dto: AddCarDto): Cars {
        val model = cacheService.getCarModel(dto.manufacturer, dto.model)
        return carRepository.save(dto, model)
    }

    @Transactional(readOnly = true)
    fun getAllCars(pageable: Pageable) = carRepository.findAll(pageable)

    @Transactional(readOnly = true)
    fun getCarDetails(id: UUID): CarDetailsDto {
        val car = carRepository.findById(id)
        return CarDetailsDto(
            car,
            checkUpService.isCheckUpNecessary(car.id)
        )
    }

    @Transactional
    fun deleteCar(id: UUID) {
        carRepository.deleteById(id)
    }
}
