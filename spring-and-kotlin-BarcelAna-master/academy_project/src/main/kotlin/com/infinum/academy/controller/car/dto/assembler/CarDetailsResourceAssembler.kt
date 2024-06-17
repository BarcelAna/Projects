package com.infinum.academy.controller.car.dto.assembler

import com.infinum.academy.controller.car.CarController
import com.infinum.academy.controller.car.dto.CarDetailsDto
import com.infinum.academy.controller.checkup.CarCheckUpController
import nu.studer.sample.tables.pojos.CheckUps
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.Year
import java.util.*

@Component
class CarDetailsResourceAssembler : RepresentationModelAssemblerSupport<CarDetailsDto, CarDetailsResource>(
    CarController::class.java,
    CarDetailsResource::class.java
) {
    private val noPagination = Pageable.unpaged()
    private val nullAssembler = PagedResourcesAssembler<CheckUps>(null, null)
    override fun toModel(entity: CarDetailsDto): CarDetailsResource {
        return createModelWithId(entity.id, entity).apply {
            add(
                linkTo<CarCheckUpController> {
                    getAllCheckUpsForCar(entity.id, "asc", noPagination, nullAssembler)
                }.withRel("checkup")
            )
        }
    }

    override fun instantiateModel(entity: CarDetailsDto): CarDetailsResource {
        return CarDetailsResource(
            id = entity.id,
            manufacturer = entity.manufacturer,
            model = entity.model,
            productionYear = entity.productionYear,
            addedAt = entity.addedAt,
            vin = entity.vin,
            isCheckUpNecessary = entity.isCheckUpNecessary
        )
    }
}

@Relation(collectionRelation = IanaLinkRelations.ITEM_VALUE)
data class CarDetailsResource(
    val id: UUID,
    val manufacturer: String,
    val model: String,
    val productionYear: Year,
    val addedAt: LocalDate,
    val vin: String,
    val isCheckUpNecessary: Boolean
) : RepresentationModel<CarDetailsResource>()
