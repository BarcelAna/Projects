package com.infinum.academy

import com.infinum.academy.model.Car
import com.infinum.academy.model.CarCheckUp
import com.infinum.academy.repository.InMemoryCarCheckUpRepository
import com.infinum.academy.service.CarCheckUpSystem
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class CarCheckUpSystemTest {

    private val carCheckUpRepository = mockk<InMemoryCarCheckUpRepository>()
    private lateinit var carCheckUpSystem: CarCheckUpSystem

    @BeforeEach
    fun setUp() {
        carCheckUpSystem = CarCheckUpSystem(carCheckUpRepository)

        every { carCheckUpRepository.findAll() }.returns(
            mapOf(
                Pair(1L, CarCheckUp(1L, LocalDateTime.now(), Car("Porsche", "Taycan", "P0ZZZ99ZLS258179"))),
                Pair(2L, CarCheckUp(2L, LocalDateTime.now(), Car("Porsche", "Taycan", "P0ZZZ99ZLS258179"))),
                Pair(3L, CarCheckUp(3L, LocalDateTime.now(), Car("MINI Cooper", "Cooper SD", "AAAA333444TTTLKO"))),
                Pair(4L, CarCheckUp(4L, LocalDateTime.now().minusYears(3), Car("MINI Cooper", "Cooper SD", "AAAA333444TTTLKO"))),
                Pair(5L, CarCheckUp(5L, LocalDateTime.now().minusYears(4), Car("MINI Cooper", "Cooper SD", "AAAA333444TTTLKO"))),
                Pair(6L, CarCheckUp(6L, LocalDateTime.now(), Car("Porsche", "Taycan", "LPOKKKKJJ1112223"))),
                Pair(7L, CarCheckUp(7L, LocalDateTime.now(), Car("Porsche", "Taycan", "LPOKKKKJJ1112223"))),
                Pair(8L, CarCheckUp(8L, LocalDateTime.now(), Car("Porsche", "Taycan", "LPOKKKKJJ1112223"))),
                Pair(9L, CarCheckUp(9L, LocalDateTime.now().minusYears(2), Car("MINI Cooper", "Cooper SD", "LKOTZU67522WXY42"))),
                Pair(10L, CarCheckUp(10L, LocalDateTime.now().minusYears(3), Car("MINI Cooper", "Cooper SD", "LKOTZU67522WXY42")))
            )
        )
    }

    @Test
    fun isCheckUpNecessaryTest() {
        assertThat(carCheckUpSystem.isCheckUpNecessary("P0ZZZ99ZLS258179")).isFalse
        assertThat(carCheckUpSystem.isCheckUpNecessary("LKOTZU67522WXY42")).isTrue
        assertThat(carCheckUpSystem.isCheckUpNecessary("AAAABBBBCCCCDDDD")).isTrue
    }

    @Test
    fun addCheckUpTest() {
        every { carCheckUpRepository.insert(any(), any()) }.returns(11L)
        every { carCheckUpRepository.findById(any()) }.returns(CarCheckUp(11L, LocalDateTime.now(), Car("Porsche", "Taycan", "P0ZZZ99ZLS258179")))
        val carCheckUp = carCheckUpSystem.addCheckUp("P0ZZZ99ZLS258179")

        assertThat(carCheckUp.id).isEqualTo(11L)
        assertThat(carCheckUp.car.manufacturer).isEqualTo("Porsche")
        assertThat(carCheckUp.car.model).isEqualTo("Taycan")
        assertThat(carCheckUp.car.vin).isEqualTo("P0ZZZ99ZLS258179")
    }

    @Test
    fun addCheckUpExceptionTest() {
        assertThatThrownBy { carCheckUpSystem.addCheckUp("AAAABBBBCCCCDDDD") }
            .isInstanceOf(NoSuchElementException::class.java)
            .hasMessage("Car with the given vin does not exist")
    }

    @Test
    fun getCheckUpsTest() {
        val checkUps = carCheckUpSystem.getCheckUps("P0ZZZ99ZLS258179")
        assertThat(checkUps.size).isEqualTo(2)
        assertThat(checkUps[0].id).isEqualTo(1L)
        assertThat(checkUps[0].car.manufacturer).isEqualTo("Porsche")
    }

    @Test
    fun getCheckUpsExceptionTest() {
        assertThatThrownBy { carCheckUpSystem.getCheckUps("AAAABBBBCCCCDDDD") }
            .isInstanceOf(NoSuchElementException::class.java)
            .hasMessage("Car with the given vin does not exist")
    }

    @Test
    fun countCheckUpsTest() {
        assertThat(carCheckUpSystem.countCheckUps("Porsche")).isEqualTo(5)
        assertThat(carCheckUpSystem.countCheckUps("MINI Cooper")).isEqualTo(5)
        assertThat(carCheckUpSystem.countCheckUps("Toyota")).isEqualTo(0)
    }
}
