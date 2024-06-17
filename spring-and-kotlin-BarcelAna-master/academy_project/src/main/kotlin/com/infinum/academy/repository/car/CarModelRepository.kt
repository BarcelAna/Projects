package com.infinum.academy.repository.car

import nu.studer.sample.tables.CarModels
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CarModelRepository(val dslContext: DSLContext) {

    val models = CarModels()
    fun save(manufacturer: String, model: String): nu.studer.sample.tables.pojos.CarModels =
        dslContext
            .insertInto(models)
            .set(models.ID, UUID.randomUUID())
            .set(models.MANUFACTURER_NAME, manufacturer)
            .set(models.MODEL_NAME, model)
            .returningResult(models.ID, models.MANUFACTURER_NAME, models.MODEL_NAME)
            .fetchInto(nu.studer.sample.tables.pojos.CarModels::class.java)
            .first()

    fun findByManufacturerNameAndModelName(manufacturerName: String, modelName: String): nu.studer.sample.tables.pojos.CarModels =
        dslContext
            .select(models.ID, models.MANUFACTURER_NAME, models.MODEL_NAME)
            .from(models)
            .where(models.MANUFACTURER_NAME.eq(manufacturerName))
            .and(models.MODEL_NAME.eq(modelName))
            .fetchInto(nu.studer.sample.tables.pojos.CarModels::class.java)
            .first()

    fun findById(id: UUID): nu.studer.sample.tables.pojos.CarModels =
        dslContext
            .select()
            .from(models)
            .where(models.ID.eq(id))
            .fetchInto(nu.studer.sample.tables.pojos.CarModels::class.java)
            .first()
}
