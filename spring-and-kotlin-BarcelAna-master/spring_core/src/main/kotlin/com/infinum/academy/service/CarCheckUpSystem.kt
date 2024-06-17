package com.infinum.academy.service

import com.infinum.academy.model.Car
import com.infinum.academy.model.CarCheckUp
import com.infinum.academy.repository.CarCheckUpRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CarCheckUpSystem(val carCheckUpRepository: CarCheckUpRepository) {

    var cars = mutableListOf<Car>()

    init {
        cars.add(Car("Porsche", "Taycan", "P0ZZZ99ZLS258179"))
        cars.add(Car("MINI Cooper", "Cooper SD", "AAAA333444TTTLKO"))
        cars.add(Car("Porsche", "Taycan", "LPOKKKKJJ1112223"))
        cars.add(Car("MINI Cooper", "Cooper SD", "LKOTZU67522WXY42"))
    }

    fun isCheckUpNecessary(vin: String) = carCheckUpRepository.findAll().map { it.value }.none { chUp ->
        chUp.car.vin == vin && chUp.performedAt.plusYears(1).isAfter(
            LocalDateTime.now()
        )
    }

    fun addCheckUp(vin: String): CarCheckUp {
        val car = cars.find { c -> c.vin == vin } ?: throw NoSuchElementException("Car with the given vin does not exist")
        val id = carCheckUpRepository.insert(LocalDateTime.now(), car)
        return carCheckUpRepository.findById(id)
    }

    fun getCheckUps(vin: String): List<CarCheckUp> {
        val checkUps = carCheckUpRepository.findAll().map { it.value }.filter { chUp -> chUp.car.vin == vin }
        if (checkUps.isEmpty()) {
            throw NoSuchElementException("Car with the given vin does not exist")
        }
        return checkUps
    }

    fun countCheckUps(manufacturer: String) = carCheckUpRepository.findAll().count { chUp -> chUp.value.car.manufacturer == manufacturer }
}
