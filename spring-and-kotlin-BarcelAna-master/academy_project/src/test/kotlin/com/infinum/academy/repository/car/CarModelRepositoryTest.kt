package com.infinum.academy.repository.car

import org.assertj.core.api.Assertions.assertThat
import org.jooq.DSLContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jooq.JooqTest

@JooqTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CarModelRepositoryTest @Autowired constructor(
    dsl: DSLContext
) {
    val modelRepository = CarModelRepository(dsl)

    @BeforeEach
    fun setup() {
        modelRepository.save("Porsche", "Panamera")
        modelRepository.save("Citroen", "C5")
    }

    @Test
    fun saveTest() {
        val model = modelRepository.save("MINI Cooper", "SD Cooper")
        assertThat(model).isNotNull
        assertThat(model.manufacturerName).isEqualTo("MINI Cooper")
    }

    @Test
    fun saveDuplicateManufacturerAndModelTest() {
        assertThrows<Exception> {
            modelRepository.save("Citroen", "C5")
        }
    }

    @Test
    fun `save duplicate manufacturer but different model test`() {
        assertDoesNotThrow {
            modelRepository.save("Citroen", "C3")
        }
    }

    @Test
    fun findByManufacturerNameAndModelNameTest() {
        val model = modelRepository.findByManufacturerNameAndModelName("Porsche", "Panamera")
        assertThat(model).isNotNull
        assertThat(model.modelName).isEqualTo("Panamera")
    }

    @Test
    fun `find by not existing manufacturer and model name test`() {
        assertThrows<Exception> {
            modelRepository.findByManufacturerNameAndModelName("MINI Cooper", "Cooper SD")
        }
    }
}
