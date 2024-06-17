package com.infinum.academy.controller.car

import com.infinum.academy.controller.car.dto.AddCarDto
import com.infinum.academy.controller.car.dto.assembler.CarDetailsResourceAssembler
import com.infinum.academy.controller.car.dto.assembler.CarResourceAssembler
import com.infinum.academy.service.car.CarService
import nu.studer.sample.tables.pojos.Cars
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.http.CacheControl
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.time.Duration
import java.util.*
import javax.validation.Valid

@RequestMapping("/car")
@Controller
class CarController(
    val carService: CarService,
    val carDetailsResourceAssembler: CarDetailsResourceAssembler,
    val carResourceAssembler: CarResourceAssembler
) {
    @PostMapping
    fun addCar(
        @Valid @RequestBody
        dto: AddCarDto
    ): ResponseEntity<Unit> {
        val car = carService.addCar(dto)

        val location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(car.id)
            .toUri()
        return ResponseEntity.created(location).build()
    }

    @GetMapping
    fun getAllCars(
        pageable: Pageable,
        pagedResourceAssembler: PagedResourcesAssembler<Cars>
    ) = ResponseEntity
        .ok()
        .cacheControl(CacheControl.maxAge(Duration.ofSeconds(10)))
        .contentType(MediaType.APPLICATION_JSON)
        .body(pagedResourceAssembler.toModel(carService.getAllCars(pageable), carResourceAssembler))

    @GetMapping("/{id}")
    fun getCarDetails(
        @PathVariable id: UUID
    ) = ResponseEntity
        .ok()
        .cacheControl(CacheControl.maxAge(Duration.ofSeconds(10)))
        .contentType(MediaType.APPLICATION_JSON)
        .body(carDetailsResourceAssembler.toModel(carService.getCarDetails(id)))

    @DeleteMapping("/{id}")
    fun deleteCar(
        @PathVariable id: UUID
    ): ResponseEntity<Unit> {
        carService.deleteCar(id)
        return ResponseEntity.noContent().build()
    }
}
