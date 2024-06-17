package com.infinum.academy.controller.checkup

import com.infinum.academy.controller.checkup.dto.AddCheckUpDto
import com.infinum.academy.controller.checkup.dto.assembler.CheckUpResourceAssembler
import com.infinum.academy.service.checkup.CheckUpService
import nu.studer.sample.tables.pojos.CheckUps
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.time.Duration
import java.util.*
import javax.validation.Valid

@Controller
@RequestMapping("/car/{id}/checkup")
class CarCheckUpController(
    val checkUpService: CheckUpService,
    val checkUpResourceAssembler: CheckUpResourceAssembler
) {
    @GetMapping
    fun getAllCheckUpsForCar(
        @PathVariable id: UUID,
        @RequestParam(value = "sort", defaultValue = "asc") sort: String,
        pageable: Pageable,
        pagedResourceAssembler: PagedResourcesAssembler<CheckUps>
    ) = ResponseEntity
        .ok()
        .cacheControl(CacheControl.maxAge(Duration.ofSeconds(10)))
        .contentType(MediaType.APPLICATION_JSON)
        .body(pagedResourceAssembler.toModel(checkUpService.getAllCheckUps(id, pageable, sort), checkUpResourceAssembler))

    @PostMapping
    fun addCheckUpForCar(
        @Valid @RequestBody
        dto: AddCheckUpDto,
        @PathVariable id: UUID
    ): ResponseEntity<Unit> {
        val checkUp = checkUpService.addCheckUp(dto, id)

        val link = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{checkupId}")
            .buildAndExpand(checkUp.id)
            .toUri()
        return ResponseEntity.created(link).build()
    }

    @GetMapping("/{checkupId}")
    fun getCheckUp(@PathVariable id: UUID, @PathVariable checkupId: UUID) = ResponseEntity
        .ok()
        .cacheControl(CacheControl.maxAge(Duration.ofSeconds(10)))
        .contentType(MediaType.APPLICATION_JSON)
        .body(checkUpResourceAssembler.toModel(checkUpService.getCheckUp(checkupId)))

    @DeleteMapping("/{checkupId}")
    fun deleteCheckUp(
        @PathVariable checkupId: UUID
    ): ResponseEntity<Unit> {
        checkUpService.deleteCheckUp(checkupId)
        return ResponseEntity.ok().build()
    }
}
