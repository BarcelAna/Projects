package com.infinum.academy.repository

import com.infinum.academy.model.Car
import com.infinum.academy.model.CarCheckUp
import java.time.LocalDateTime

interface CarCheckUpRepository {
    fun insert(performedAt: LocalDateTime, car: Car): Long
    fun findById(id: Long): CarCheckUp
    fun deleteById(id: Long): CarCheckUp
    fun findAll(): Map<Long, CarCheckUp>
}
