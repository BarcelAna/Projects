package com.infinum.academy.controller.checkup

import com.fasterxml.jackson.databind.ObjectMapper
import com.infinum.academy.configuration.SecurityConfiguration
import com.infinum.academy.controller.car.dto.assembler.CarDetailsResourceAssembler
import com.infinum.academy.controller.car.dto.assembler.CarResourceAssembler
import com.infinum.academy.controller.checkup.dto.AddCheckUpDto
import com.infinum.academy.controller.checkup.dto.AnalyticsProjection
import com.infinum.academy.controller.checkup.dto.assembler.CheckUpResource
import com.infinum.academy.controller.checkup.dto.assembler.CheckUpResourceAssembler
import com.infinum.academy.service.car.CarService
import com.infinum.academy.service.checkup.CheckUpService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import nu.studer.sample.tables.pojos.CheckUps
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageImpl
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDateTime
import java.util.*

@WebMvcTest
@Import(SecurityConfiguration::class)
class CheckUpControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var carCheckUpService: CheckUpService

    @MockkBean
    private lateinit var carService: CarService

    @MockkBean
    private lateinit var carDetailsResourceAssembler: CarDetailsResourceAssembler

    @MockkBean
    private lateinit var carResourceAssembler: CarResourceAssembler

    @MockkBean
    private lateinit var checkUpResourceAssembler: CheckUpResourceAssembler

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun addCarCheckUpTest() {
        every { carCheckUpService.addCheckUp(any(), any()) } answers { CheckUps(UUID.randomUUID(), "Bob", 200F, LocalDateTime.now(), UUID.randomUUID()) }

        mockMvc.perform(
            post("/car/0b993047-5c63-4d73-a354-ed23914b3e39/checkup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AddCheckUpDto("Bob", 20)))
        )
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
    }

    @Test
    @WithAnonymousUser
    fun analyticsTest() {
        every { carCheckUpService.getAnalytics() } returns listOf(
            AnalyticsProjection("MINI Cooper", 2),
            AnalyticsProjection("Porsche", 1)
        )

        mockMvc.get("/checkup/analytics")
            .andExpect {
                status { is2xxSuccessful() }
            }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun getAllCheckUpsForCar() {
        every { checkUpResourceAssembler.toModel(any()) } returns CheckUpResource(UUID.randomUUID(), UUID.randomUUID(), "Bob", LocalDateTime.now(), 200F)
        every { carCheckUpService.getAllCheckUps(any(), any(), any()) } returns PageImpl(
            listOf(
                CheckUps(UUID.randomUUID(), "Bob", 200F, LocalDateTime.now(), UUID.randomUUID()),
                CheckUps(UUID.randomUUID(), "Bob", 200F, LocalDateTime.now(), UUID.randomUUID())
            )
        )
        mockMvc.get("/car/4d1303f3-0077-4076-b1b2-4082245ad746/checkup")
            .andExpect {
                status { is2xxSuccessful() }
            }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    fun deleteCheckUpTest() {
        every { carCheckUpService.deleteCheckUp(any()) } returns Unit
        mockMvc.delete("/car/4d1303f3-0077-4076-b1b2-4082245ad746/checkup/7aa303f3-4477-4076-bbb2-4082245ad746")
            .andExpect {
                status { is2xxSuccessful() }
            }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_USER"])
    fun `can not delete checkup withou ADMIN scope test`() {
        every { carCheckUpService.deleteCheckUp(any()) } returns Unit
        mockMvc.delete("/car/4d1303f3-0077-4076-b1b2-4082245ad746/checkup/7aa303f3-4477-4076-bbb2-4082245ad746")
            .andExpect {
                status { isForbidden() }
            }
    }
}
