package com.infinum.academy.repository.car

import com.infinum.academy.controller.car.dto.AddCarDto
import org.assertj.core.api.Assertions.assertThat
import org.jooq.DSLContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.springframework.data.domain.PageRequest
import java.time.Year
import java.util.*

@JooqTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CarRepositoryTest @Autowired constructor(
    dsl: DSLContext
) {

    val carRepository = CarRepository(dsl)
    val modelRepository = CarModelRepository(dsl)

    lateinit var testId: UUID

    @BeforeEach
    fun setUp() {
        val model1 = modelRepository.save("Porsche", "Panamera")
        val model2 = modelRepository.save("Citroen", "C3")

        val c1 = AddCarDto(manufacturer = "Porsche", model = "Panamera", productionYear = Year.of(2022), vin = "AAAABBBBCCCCDDDD")
        val c2 = AddCarDto(manufacturer = "Citroen", model = "C3", productionYear = Year.of(2005), vin = "FFFFGGGGHHHHIIII")
        val c3 = AddCarDto(manufacturer = "Citroen", model = "C3", productionYear = Year.of(2021), vin = "JJJJKKKKLLLLMMMM")
        val c4 = AddCarDto(manufacturer = "Porsche", model = "Panamera", productionYear = Year.of(2020), vin = "NNNNOOOOPPPPRRRR")

        val savedCar = carRepository.save(c1, model1)
        carRepository.save(c2, model2)
        carRepository.save(c3, model2)
        carRepository.save(c4, model1)

        testId = savedCar.id
    }

    @Test
    fun findAllTest() {
        val cars = carRepository.findAll()
        assertThat(cars).isNotNull
        assert(cars.size == 4)
        assertThat(cars[0].vin).isEqualTo("AAAABBBBCCCCDDDD")
    }

    @Test
    fun findAllPagedTest() {
        val pageable = PageRequest.of(0, 2)
        val cars = carRepository.findAll(pageable)
        assertThat(cars.totalPages).isEqualTo(2)
        assertThat(cars.content[0].vin).isEqualTo("AAAABBBBCCCCDDDD")
    }

    @Test
    fun findByIdTest() {
        val found = carRepository.findById(testId)
        assertThat(found).isNotNull
        assertThat(found.productionYear == Year.of(2022))
        assertThat(found.vin).isEqualTo("AAAABBBBCCCCDDDD")
        assertThat(found.manufacturer).isEqualTo("Porsche")
    }

    @Test
    fun findByNotExistingIdTest() {
        assertThrows<Exception> { carRepository.findById(UUID.randomUUID()) }
    }

    @Test
    fun saveTest() {
        val car = AddCarDto(manufacturer = "Citroen", model = "C3", productionYear = Year.of(2021), vin = "SSSSTTTTUUUUVVVV")
        val model = modelRepository.findByManufacturerNameAndModelName("Citroen", "C3")
        val savedCar = carRepository.save(car, model)
        assertThat(savedCar).isNotNull
        assertThat(savedCar.vin).isEqualTo("SSSSTTTTUUUUVVVV")
    }

    @Test
    fun saveDuplicateCarTest() {
        val model = modelRepository.findByManufacturerNameAndModelName("Citroen", "C3")

        assertThrows<Exception> {
            carRepository.save(AddCarDto(manufacturer = "Citroen", model = "C3", productionYear = Year.of(2021), vin = "FFFFGGGGHHHHIIII"), model)
        }
    }

    @Test
    fun deleteByIdTest() {
        carRepository.deleteById(testId)
        assertThat(carRepository.findAll().size == 3)
    }
}
