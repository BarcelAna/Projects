package com.infinum.academy.controller.car

import com.fasterxml.jackson.databind.ObjectMapper
import com.infinum.academy.configuration.SecurityConfiguration
import com.infinum.academy.controller.car.dto.AddCarDto
import com.infinum.academy.controller.car.dto.CarDetailsDto
import com.infinum.academy.controller.car.dto.assembler.CarDetailsResource
import com.infinum.academy.controller.car.dto.assembler.CarDetailsResourceAssembler
import com.infinum.academy.controller.car.dto.assembler.CarResource
import com.infinum.academy.controller.car.dto.assembler.CarResourceAssembler
import com.infinum.academy.controller.checkup.dto.assembler.CheckUpResourceAssembler
import com.infinum.academy.service.car.CarService
import com.infinum.academy.service.checkup.CheckUpService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import nu.studer.sample.tables.pojos.Cars
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageImpl
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDate
import java.time.Year
import java.util.*

@WebMvcTest
@Import(SecurityConfiguration::class)
class CarControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var carService: CarService

    @MockkBean
    private lateinit var checkUpService: CheckUpService

    @MockkBean
    private lateinit var checkUpResourceAssembler: CheckUpResourceAssembler

    @MockkBean
    private lateinit var carDetailsResourceAssembler: CarDetailsResourceAssembler

    @MockkBean
    private lateinit var carResourceAssembler: CarResourceAssembler

    @Test
    @WithMockUser(authorities = ["SCOPE_USER"])
    fun addCarTest() {
        every { carService.addCar(any()) } answers { Cars(UUID.randomUUID(), 2020, "AAAABBBBCCCCDDDD", LocalDate.now(), UUID.randomUUID()) }

        mockMvc.perform(
            post("/car")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AddCarDto("Porsche", "Taycan", Year.of(2020), "AAAAEEEECCCCDDDD")))
        )
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_USER"])
    fun addCarWithWrongVinTest() {
        mockMvc.perform(
            post("/car")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AddCarDto("Porsche", "Taycan", Year.of(2020), "AAAAEEEECCCC")))
        )
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_USER"])
    fun addCarWithNoManufacturerTest() {
        mockMvc.perform(
            post("/car")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AddCarDto("", "Taycan", Year.of(2020), "AAAAEEEECCCC")))
        )
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }

    @WithMockUser(authorities = ["SCOPE_USER"])
    @Test
    fun carDetailsTest() {
        every { carDetailsResourceAssembler.toModel(any()) } returns CarDetailsResource(UUID.randomUUID(), "Porsche", "Taycan", Year.of(2020), LocalDate.now(), "AAAABBBBCCCCDDDD", false)
        val carDetails = CarDetailsDto(UUID.randomUUID(), "Porsche", "Taycan", Year.of(2020), LocalDate.now(), "AAAABBBBCCCCDDDD", false)
        every { carService.getCarDetails(any()) } answers { carDetails }

        val carDetailsJson = """
            {
                "manufacturer": "Porsche",
                "model": "Taycan",
                "productionYear": 2020,
                "vin": "AAAABBBBDDDDEEEE",
                "checkUps": [],
                "isCheckUpNecessary": true
        """.trimIndent()

        mockMvc
            .get("/car/4d1303f3-0077-4076-b1b2-4082245ad746")
            .andExpect {
                status { is2xxSuccessful() }
                content { carDetailsJson }
            }
    }

    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    @Test
    fun getAllCarsTest() {
        every { carResourceAssembler.toModel(any()) } returns CarResource(UUID.randomUUID(), Year.of(2022), "AAAABBBBCCCCDDDD", LocalDate.now(), UUID.randomUUID())
        every { carService.getAllCars(any()) } returns PageImpl(
            listOf(
                Cars(UUID.randomUUID(), 2022, "AAAABBBBCCCCDDDD", LocalDate.now(), UUID.randomUUID()),
                Cars(UUID.randomUUID(), 2022, "DDDDEEEEFFFFGGGG", LocalDate.now(), UUID.randomUUID())
            )
        )

        mockMvc.get("/car")
            .andExpect {
                status { is2xxSuccessful() }
            }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun deleteCarTest() {
        every { carService.deleteCar(any()) } returns Unit
        mockMvc.delete("/car/4d1303f3-0077-4076-b1b2-4082245ad746")
            .andExpect {
                status { is2xxSuccessful() }
            }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_USER"])
    fun `can not delete car without ADMIN scope`() {
        every { carService.deleteCar(any()) } returns Unit
        mockMvc.delete("/car/4d1303f3-0077-4076-b1b2-4082245ad746")
            .andExpect {
                status { isForbidden() }
            }
    }
}
