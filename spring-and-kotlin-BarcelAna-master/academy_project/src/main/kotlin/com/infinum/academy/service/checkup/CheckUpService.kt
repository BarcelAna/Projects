package com.infinum.academy.service.checkup

import com.infinum.academy.controller.checkup.dto.AddCheckUpDto
import com.infinum.academy.repository.checkup.CheckUpRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class CheckUpService(val checkUpRepository: CheckUpRepository) {

    @Transactional
    fun addCheckUp(dto: AddCheckUpDto, carId: UUID) = checkUpRepository.save(dto, carId)

    @Transactional(readOnly = true)
    fun getAnalytics() = checkUpRepository.countCheckUpsByManufacturer()

    @Transactional(readOnly = true)
    fun isCheckUpNecessary(carId: UUID) = checkUpRepository.findByCarId(carId)
        .none { chUp ->
            chUp.carId == carId && chUp.performedAt.plusYears(1).isAfter(
                LocalDateTime.now()
            )
        }

    @Transactional(readOnly = true)
    fun getAllCheckUps(id: UUID, pageable: Pageable, sort: String?) = checkUpRepository.findByCarId(id, pageable, sort)

    @Transactional(readOnly = true)
    fun getCheckUp(id: UUID) = checkUpRepository.findById(id)

    @Transactional
    fun deleteCheckUp(checkupId: UUID) {
        checkUpRepository.deleteById(checkupId)
    }
}
