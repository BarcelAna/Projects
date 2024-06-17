package com.infinum.academy.repository.checkup

import com.infinum.academy.controller.car.dto.AddCarDto
import com.infinum.academy.controller.checkup.dto.AddCheckUpDto
import com.infinum.academy.repository.car.CarModelRepository
import com.infinum.academy.repository.car.CarRepository
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
import java.util.UUID

@JooqTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CheckUpRepositoryTest @Autowired constructor(
    dsl: DSLContext
) {

    val carRepository = CarRepository(dsl)
    val carCheckUpRepository = CheckUpRepository(dsl)
    val modelRepository = CarModelRepository(dsl)

    lateinit var testId: UUID
    lateinit var testCarId: UUID

    @BeforeEach
    fun setUp() {
        val model1 = modelRepository.save("Porsche", "Panamera")
        val model2 = modelRepository.save("Citroen", "C3")

        val car1 = AddCarDto(manufacturer = "Porsche", model = "Panamera", productionYear = Year.of(2022), vin = "AAAABBBBCCCCDDDD")
        val car2 = AddCarDto(manufacturer = "Citroen", model = "C3", productionYear = Year.of(2005), vin = "FFFFGGGGHHHHIIII")

        val savedCar1 = carRepository.save(car1, model1)
        val savedCar2 = carRepository.save(car2, model2)

        val checkup1 = AddCheckUpDto(workerName = "Bob", price = 200)
        val checkup2 = AddCheckUpDto(workerName = "Ana", price = 300)
        val checkup3 = AddCheckUpDto(workerName = "Bob", price = 100)

        val savedCheckUp1 = carCheckUpRepository.save(checkup1, savedCar1.id)
        carCheckUpRepository.save(checkup2, savedCar1.id)
        carCheckUpRepository.save(checkup3, savedCar2.id)

        testId = savedCheckUp1.id
        testCarId = savedCar1.id
    }

    @Test
    fun findAllTest() {
        val checkups = carCheckUpRepository.findAll()
        assertThat(checkups).isNotNull
        assertThat(checkups.size).isEqualTo(3)
    }

    @Test
    fun findAllPagedTest() {
        val pageable = PageRequest.of(0, 2)
        val checkups = carCheckUpRepository.findAll(pageable)
        assertThat(checkups.totalPages).isEqualTo(2)
        assertThat(checkups.content[0].workerName).isEqualTo("Bob")
    }

    @Test
    fun findByIdTest() {
        val found = carCheckUpRepository.findById(testId)
        assertThat(found).isNotNull
        assertThat(found.workerName).isEqualTo("Bob")
        assertThat(found.price).isEqualTo(200f)
    }

    @Test
    fun findByNotExistingIdTest() {
        assertThrows<Exception> {
            carCheckUpRepository.findById(UUID.randomUUID())
        }
    }

    @Test
    fun saveTest() {
        val model1 = modelRepository.findByManufacturerNameAndModelName("Porsche", "Panamera")
        val car = carRepository.save(AddCarDto(manufacturer = "Porsche", model = "Panamera", productionYear = Year.of(2022), vin = "OOOOPPPPRRRRSSSS"), model1)
        val checkup = carCheckUpRepository.save(AddCheckUpDto(workerName = "Bob", price = 200), car.id)
        assertThat(checkup).isNotNull
        assertThat(checkup.workerName).isEqualTo("Bob")
        assertThat(checkup.carId).isEqualTo(car.id)
    }

    @Test
    fun saveCheckUpForNotExistingCarTest() {
        assertThrows<Exception> {
            carCheckUpRepository.save(AddCheckUpDto(workerName = "Bob", price = 200), UUID.randomUUID())
        }
    }

    @Test
    fun deleteByIdTest() {
        carCheckUpRepository.deleteById(testId)
        assertThat(carCheckUpRepository.findAll().size == 2)
    }

    @Test
    fun findByCarIdTest() {
        val checkups = carCheckUpRepository.findByCarId(testCarId)
        assertThat(checkups).isNotNull
        assertThat(checkups.size).isEqualTo(2)
    }

    @Test
    fun findByCarIdPagedTest() {
        val pageable = PageRequest.of(0, 2)
        val checkups = carCheckUpRepository.findByCarId(testCarId, pageable, "")
        assertThat(checkups.totalPages).isEqualTo(1)
        assertThat(checkups.content[0].workerName).isEqualTo("Bob")
    }

    @Test
    fun findByNotExistingCarIdTest() {
        val checkups = carCheckUpRepository.findByCarId(UUID.randomUUID())
        assertThat(checkups).isEmpty()
    }

    @Test
    fun countCheckUpsByManufacturerTest() {
        val list = carCheckUpRepository.countCheckUpsByManufacturer()
        assertThat(list).isNotNull
        assertThat(list[0].manufacturer).isEqualTo("Porsche")
        assertThat(list[0].count).isEqualTo(2)
        assertThat(list[1].count).isEqualTo(1)
    }
}
