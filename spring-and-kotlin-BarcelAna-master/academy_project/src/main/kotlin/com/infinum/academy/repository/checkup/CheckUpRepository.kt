package com.infinum.academy.repository.checkup

import com.infinum.academy.controller.checkup.dto.AddCheckUpDto
import com.infinum.academy.controller.checkup.dto.AnalyticsProjection
import nu.studer.sample.tables.CarModels
import nu.studer.sample.tables.Cars
import nu.studer.sample.tables.CheckUps
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
class CheckUpRepository(private var dsl: DSLContext) {

    val checkUps = CheckUps()
    val cars = Cars()
    val models = CarModels()

    fun save(checkUp: AddCheckUpDto, id: UUID): nu.studer.sample.tables.pojos.CheckUps {
        return dsl.insertInto(checkUps)
            .set(checkUps.ID, UUID.randomUUID())
            .set(checkUps.WORKER_NAME, checkUp.workerName)
            .set(checkUps.PRICE, checkUp.price.toFloat())
            .set(checkUps.PERFORMED_AT, LocalDateTime.now())
            .set(checkUps.CAR_ID, id)
            .returningResult(checkUps.ID, checkUps.WORKER_NAME, checkUps.PRICE, checkUps.PERFORMED_AT, checkUps.CAR_ID)
            .fetchInto(nu.studer.sample.tables.pojos.CheckUps::class.java).first()
    }

    fun findById(id: UUID): nu.studer.sample.tables.pojos.CheckUps =
        dsl.select(checkUps.ID, checkUps.WORKER_NAME, checkUps.PRICE, checkUps.PERFORMED_AT, checkUps.CAR_ID)
            .from(checkUps)
            .where(checkUps.ID.eq(id))
            .fetchInto(nu.studer.sample.tables.pojos.CheckUps::class.java).first()

    fun findByCarId(carId: UUID): List<nu.studer.sample.tables.pojos.CheckUps> =
        dsl.select(checkUps.ID, checkUps.WORKER_NAME, checkUps.PRICE, checkUps.PERFORMED_AT, checkUps.CAR_ID)
            .from(checkUps)
            .where(checkUps.CAR_ID.eq(carId))
            .fetchInto(nu.studer.sample.tables.pojos.CheckUps::class.java)

    fun findByCarId(carId: UUID, pageable: Pageable, sort: String?): Page<nu.studer.sample.tables.pojos.CheckUps> {
        val list = dsl.select(checkUps.ID, checkUps.WORKER_NAME, checkUps.PRICE, checkUps.PERFORMED_AT, checkUps.CAR_ID)
            .from(checkUps)
            .where(checkUps.CAR_ID.eq(carId))
            .orderBy(checkUps.PERFORMED_AT)
            .fetchInto(nu.studer.sample.tables.pojos.CheckUps::class.java)
        if (sort == "desc") {
            list.reverse()
        }
        return PageImpl(list, pageable, list.size.toLong())
    }

    fun countCheckUpsByManufacturer(): List<AnalyticsProjection> =
        dsl.select(models.MANUFACTURER_NAME, DSL.count())
            .from(cars)
            .innerJoin(models)
            .on(cars.CAR_MODEL_ID.eq(models.ID))
            .innerJoin(checkUps)
            .on(cars.ID.eq(checkUps.CAR_ID))
            .groupBy(models.MANUFACTURER_NAME)
            .fetch()
            .map {
                AnalyticsProjection(
                    manufacturer = it["manufacturer_name"] as String,
                    count = it["count"] as Int
                )
            }

    fun findAll(): List<nu.studer.sample.tables.pojos.CheckUps> =
        dsl.select(checkUps.ID, checkUps.WORKER_NAME, checkUps.PRICE, checkUps.PERFORMED_AT, checkUps.CAR_ID)
            .from(checkUps)
            .fetchInto(nu.studer.sample.tables.pojos.CheckUps::class.java)

    fun findAll(pageable: Pageable): Page<nu.studer.sample.tables.pojos.CheckUps> {
        val list = dsl.select(checkUps.ID, checkUps.WORKER_NAME, checkUps.PRICE, checkUps.PERFORMED_AT, checkUps.CAR_ID)
            .from(checkUps)
            .fetchInto(nu.studer.sample.tables.pojos.CheckUps::class.java)
        return PageImpl(list, pageable, list.size.toLong())
    }

    fun deleteById(id: UUID) =
        dsl
            .deleteFrom(checkUps)
            .where(checkUps.ID.eq(id))
            .execute()
}
