package com.infinum.academy

import com.fasterxml.jackson.databind.ObjectMapper
import com.infinum.academy.controller.car.dto.AddCarDto
import com.infinum.academy.controller.checkup.dto.AddCheckUpDto
import com.infinum.academy.controller.checkup.dto.AnalyticsProjection
import com.infinum.academy.repository.car.CarModelRepository
import com.infinum.academy.repository.car.CarRepository
import com.infinum.academy.repository.checkup.CheckUpRepository
import com.infinum.academy.service.car.WebClientCarsService
import com.infinum.academy.service.checkup.CheckUpService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.Year
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class IntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var carCheckUpService: CheckUpService

    @Autowired
    private lateinit var apiService: WebClientCarsService

    @Autowired
    private lateinit var carRepository: CarRepository

    @Autowired
    private lateinit var checkUpRepository: CheckUpRepository

    @Autowired
    private lateinit var modelRepository: CarModelRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    lateinit var testCarId: UUID

    @BeforeEach
    fun fetchCarsAndManufacturers() {
        apiService.updateManufacturersAndModels()
        val car = carRepository.save(AddCarDto("Porsche", "Panamera", Year.of(2020), "AAAAEEEECCCCDDDD"), modelRepository.findByManufacturerNameAndModelName("Porsche", "Panamera"))
        testCarId = car.id
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun addCarTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/car")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AddCarDto("Porsche", "Panamera", Year.of(2020), "BBBBGGGGHHHHWWWW")))
        )
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
    }

    @Test
    @WithAnonymousUser
    fun addCarAsAnonymousTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/car")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AddCarDto("Porsche", "Panamera", Year.of(2020), "BBBBGGGGHHHHWWWW")))
        )
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun addDuplicateCarTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/car")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AddCarDto("Porsche", "Panamera", Year.of(2020), "AAAAEEEECCCCDDDD")))
        )
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun addCarWithNoExistingManufacturerTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/car")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AddCarDto("MINI Cooper", "SD Cooper", Year.of(2020), "BBBBHHHHIIIIKKKK")))
        )
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun addCarCheckUpTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/car/$testCarId/checkup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AddCheckUpDto("Bob", 20)))
        )
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_USER"])
    fun `can not add checkup without ADMIN authorities test`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/car/$testCarId/checkup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AddCheckUpDto("Bob", 20)))
        )
            .andExpect(MockMvcResultMatchers.status().isForbidden)
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun addNotExistingCarCheckUpTest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/car/0b993047-5c63-4d73-aa54-ed23914b3e39/checkup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AddCheckUpDto("Bob", 20)))
        )
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_USER"])
    fun carDetailsTest() {
        mockMvc
            .get("/car/$testCarId")
            .andExpect {
                status { is2xxSuccessful() }
            }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_SOMEONE"])
    fun `can not get car details withous ADMIN or USER scope test`() {
        mockMvc
            .get("/car/$testCarId")
            .andExpect {
                status { isForbidden() }
            }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_USER"])
    fun notExistingCarDetailsTest() {
        mockMvc
            .get("/car/${UUID.randomUUID()}")
            .andExpect {
                status { is4xxClientError() }
            }
    }

    @Test
    @WithAnonymousUser
    fun analyticsTest() {
        carCheckUpService.addCheckUp(AddCheckUpDto("Bob", 20), testCarId)

        mockMvc.get("/checkup/analytics")
            .andExpect {
                status { is2xxSuccessful() }
                content {
                    objectMapper.writeValueAsString(
                        AnalyticsProjection(
                            manufacturer = "Porsche",
                            count = 1
                        )
                    )
                }
            }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun getAllCarsTest() {
        mockMvc.get("/car")
            .andExpect {
                status { is2xxSuccessful() }
            }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_USER"])
    fun `can not get all cars without ADMIN scope test`() {
        mockMvc.get("/car")
            .andExpect {
                status { isForbidden() }
            }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_USER"])
    fun getAllCheckupsForCarTest() {
        mockMvc.get("/car/$testCarId/checkup")
            .andExpect {
                status { is2xxSuccessful() }
            }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_SOMEONE"])
    fun `can not get checkups for car without ADMIN or USER scope test`() {
        mockMvc.get("/car/$testCarId/checkup")
            .andExpect {
                status { isForbidden() }
            }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun deleteCarTest() {
        assertThat(carRepository.findAll().size).isEqualTo(1)
        mockMvc.delete("/car/$testCarId")
            .andExpect {
                status { is2xxSuccessful() }
            }
        assertThat(carRepository.findAll().size).isEqualTo(0)
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_USER"])
    fun `can not delete car without ADMIN scope test`() {
        mockMvc.delete("/car/$testCarId")
            .andExpect {
                status { isForbidden() }
            }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun deleteCheckUpTest() {
        val checkup = carCheckUpService.addCheckUp(AddCheckUpDto("Bob", 20), testCarId)
        assertThat(checkUpRepository.findAll().size).isEqualTo(1)
        mockMvc.delete("/car/$testCarId/checkup/${checkup.id}")
            .andExpect {
                status { is2xxSuccessful() }
            }
        assertThat(checkUpRepository.findAll().size).isEqualTo(0)
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_USER"])
    fun `can not delete checkup without ADMIN scope test`() {
        val checkup = carCheckUpService.addCheckUp(AddCheckUpDto("Bob", 20), testCarId)
        mockMvc.delete("/car/$testCarId/checkup/${checkup.id}")
            .andExpect {
                status { isForbidden() }
            }
    }
}
