package com.infinum.academy

import com.infinum.academy.model.CarCheckUp
import com.infinum.academy.service.CarCheckUpSystem
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [ApplicationConfiguration::class])
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CarCheckUpSystemIntegrationTest @Autowired constructor(
    private val applicationContext: ApplicationContext,
    private val carCheckUpSystem: CarCheckUpSystem
) {

    @Test
    fun verifyCarCheckUpSystemService() {
        assertThat(applicationContext.getBean(CarCheckUpSystem::class.java)).isNotNull
    }

    @Test
    fun isCheckUpNecessaryTest() {
        carCheckUpSystem.addCheckUp("P0ZZZ99ZLS258179")

        assertThat(carCheckUpSystem.isCheckUpNecessary("P0ZZZ99ZLS258179")).isFalse
        assertThat(carCheckUpSystem.isCheckUpNecessary("AAAA333444TTTLKO")).isTrue
        assertThat(carCheckUpSystem.isCheckUpNecessary("AAAABBBBCCCCDDDD")).isTrue
    }

    @Test
    fun addCheckUpTest() {
        val checkUp = carCheckUpSystem.addCheckUp("LPOKKKKJJ1112223")
        assertThat(checkUp).isInstanceOf(CarCheckUp::class.java)
        assertThat(checkUp.id).isEqualTo(1L)
        assertThat(checkUp.car.manufacturer).isEqualTo("Porsche")
        assertThat(checkUp.car.model).isEqualTo("Taycan")
        assertThat(checkUp.car.vin).isEqualTo("LPOKKKKJJ1112223")
        assertThat(carCheckUpSystem.isCheckUpNecessary("LPOKKKKJJ1112223")).isFalse
    }

    @Test
    fun addCheckUpExceptionTest() {
        assertThatThrownBy { carCheckUpSystem.addCheckUp("AAAABBBBCCCCDDDD") }
            .isInstanceOf(NoSuchElementException::class.java)
            .hasMessage("Car with the given vin does not exist")

        assertThat(carCheckUpSystem.isCheckUpNecessary("AAAABBBBCCCCDDDD")).isTrue
    }

    @Test
    fun getCheckUpsTest() {
        carCheckUpSystem.addCheckUp("P0ZZZ99ZLS258179")
        carCheckUpSystem.addCheckUp("P0ZZZ99ZLS258179")

        assertThat(carCheckUpSystem.getCheckUps("P0ZZZ99ZLS258179").size).isEqualTo(2)
        assertThat(carCheckUpSystem.getCheckUps("P0ZZZ99ZLS258179")[0].car.manufacturer).isEqualTo("Porsche")
    }

    @Test
    fun getCheckUpsExceptionTest() {
        assertThatThrownBy { carCheckUpSystem.getCheckUps("AAAABBBBCCCCDDDD") }
            .isInstanceOf(NoSuchElementException::class.java)
            .hasMessage("Car with the given vin does not exist")
    }

    @Test
    fun countCheckUpsTest() {
        assertThat(carCheckUpSystem.countCheckUps("MINI Cooper")).isEqualTo(0)
        carCheckUpSystem.addCheckUp("LKOTZU67522WXY42")
        assertThat(carCheckUpSystem.countCheckUps("MINI Cooper")).isEqualTo(1)
    }
}
