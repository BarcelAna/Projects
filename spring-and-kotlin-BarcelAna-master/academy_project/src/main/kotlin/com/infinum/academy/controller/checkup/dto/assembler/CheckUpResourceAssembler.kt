package com.infinum.academy.controller.checkup.dto.assembler

import com.infinum.academy.controller.checkup.CarCheckUpController
import nu.studer.sample.tables.pojos.CheckUps
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

@Component
class CheckUpResourceAssembler : RepresentationModelAssemblerSupport<CheckUps, CheckUpResource>(
    CarCheckUpController::class.java,
    CheckUpResource::class.java
) {
    override fun toModel(entity: CheckUps): CheckUpResource {
        return createModelWithId(entity.id, entity, entity.carId)
    }

    override fun instantiateModel(entity: CheckUps): CheckUpResource {
        return CheckUpResource(
            id = entity.id,
            carId = entity.carId,
            workerName = entity.workerName,
            performedAt = entity.performedAt,
            price = entity.price
        )
    }
}

@Relation(collectionRelation = IanaLinkRelations.ITEM_VALUE)
data class CheckUpResource(
    val id: UUID,
    val carId: UUID,
    val workerName: String,
    var performedAt: LocalDateTime,
    val price: Float
) : RepresentationModel<CheckUpResource>()
