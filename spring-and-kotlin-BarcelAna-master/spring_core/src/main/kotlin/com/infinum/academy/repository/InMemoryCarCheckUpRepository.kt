package com.infinum.academy.repository

import com.infinum.academy.configuration.DataSource
import com.infinum.academy.model.Car
import com.infinum.academy.model.CarCheckUp
import com.infinum.academy.model.CarCheckUpNotFoundException
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
@Profile("inMemoryRepo")
class InMemoryCarCheckUpRepository(dataSource: DataSource) : CarCheckUpRepository {

    private val carCheckUpMap = mutableMapOf<Long, CarCheckUp>()

    init {
        println("dbName: ${dataSource.dbName}")
        println("username: ${dataSource.username}")
        println("password: ${dataSource.password}")
    }
    override fun insert(performedAt: LocalDateTime, car: Car): Long {
        val id = (carCheckUpMap.keys.maxOrNull() ?: 0L) + 1
        carCheckUpMap[id] = CarCheckUp(id, performedAt, car)
        return id
    }

    override fun findById(id: Long): CarCheckUp {
        return carCheckUpMap[id] ?: throw CarCheckUpNotFoundException(id)
    }

    override fun deleteById(id: Long): CarCheckUp {
        return carCheckUpMap.remove(id) ?: throw CarCheckUpNotFoundException(id)
    }

    override fun findAll(): Map<Long, CarCheckUp> {
        return carCheckUpMap.toMap()
    }
}
