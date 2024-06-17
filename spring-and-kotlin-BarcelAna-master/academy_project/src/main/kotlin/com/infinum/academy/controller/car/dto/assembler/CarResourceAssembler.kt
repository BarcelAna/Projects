package com.infinum.academy.controller.car.dto.assembler

import com.infinum.academy.controller.car.CarController
import nu.studer.sample.tables.pojos.Cars
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.Year
import java.util.*

@Component
class CarResourceAssembler : RepresentationModelAssemblerSupport<Cars, CarResource>(
    CarController::class.java,
    CarResource::class.java
) {
    override fun toModel(entity: Cars): CarResource {
        return createModelWithId(entity.id, entity)
    }

    override fun instantiateModel(entity: Cars): CarResource {
        return CarResource(
            id = entity.id,
            productionYear = Year.of(entity.productionYear),
            vin = entity.vin,
            addedAt = entity.addedAt,
            carModelId = entity.carModelId
        )
    }
}

@Relation(collectionRelation = IanaLinkRelations.ITEM_VALUE)
data class CarResource(
    val id: UUID,
    val productionYear: Year,
    val vin: String,
    val addedAt: LocalDate,
    val carModelId: UUID
) : RepresentationModel<CarResource>()
