package com.infinum.academy.repository.car

import com.infinum.academy.controller.car.dto.AddCarDto
import com.infinum.academy.controller.car.dto.CarDto
import nu.studer.sample.tables.CarModels
import nu.studer.sample.tables.Cars
import org.jooq.DSLContext
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.Year
import java.util.*

@Repository
class CarRepository(private var dsl: DSLContext) {

    val cars = Cars()
    val models = CarModels()

    fun save(car: AddCarDto, carModel: nu.studer.sample.tables.pojos.CarModels): nu.studer.sample.tables.pojos.Cars =
        dsl.insertInto(cars)
            .set(cars.ID, UUID.randomUUID())
            .set(cars.CAR_MODEL_ID, carModel.id)
            .set(cars.PRODUCTION_YEAR, Integer.parseInt(car.productionYear.toString()))
            .set(cars.ADDED_AT, LocalDate.now())
            .set(cars.VIN, car.vin)
            .returningResult(cars.ID, cars.PRODUCTION_YEAR, cars.VIN, cars.ADDED_AT, cars.CAR_MODEL_ID)
            .fetchInto(nu.studer.sample.tables.pojos.Cars::class.java)
            .first()

    fun findAll(): List<nu.studer.sample.tables.pojos.Cars> =
        dsl
            .select(cars.ID, cars.PRODUCTION_YEAR, cars.VIN, cars.ADDED_AT, cars.CAR_MODEL_ID)
            .from(cars)
            .fetchInto(nu.studer.sample.tables.pojos.Cars::class.java)

    fun findAll(pageable: Pageable): Page<nu.studer.sample.tables.pojos.Cars> {
        val list = dsl
            .select(cars.ID, cars.PRODUCTION_YEAR, cars.VIN, cars.ADDED_AT, cars.CAR_MODEL_ID)
            .from(cars)
            .fetchInto(nu.studer.sample.tables.pojos.Cars::class.java)
        return PageImpl(list, pageable, list.size.toLong())
    }

    fun findById(id: UUID) =
        dsl
            .select(models.MANUFACTURER_NAME, models.MODEL_NAME, cars.PRODUCTION_YEAR, cars.VIN, cars.ADDED_AT, cars.ID)
            .from(cars)
            .innerJoin(models)
            .on(cars.CAR_MODEL_ID.eq(models.ID))
            .where(cars.ID.eq(id))
            .fetch()
            .first()
            .map {
                CarDto(
                    manufacturer = it[0] as String,
                    model = it[1] as String,
                    productionYear = Year.of(it[2] as Int),
                    vin = it[3] as String,
                    addedAt = it[4] as LocalDate,
                    id = it[5] as UUID
                )
            }
    fun deleteById(id: UUID) =
        dsl
            .deleteFrom(cars)
            .where(cars.ID.eq(id))
            .execute()
}
