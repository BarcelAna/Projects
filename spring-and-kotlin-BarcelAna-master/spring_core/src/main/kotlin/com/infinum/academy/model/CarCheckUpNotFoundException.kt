package com.infinum.academy.model

class CarCheckUpNotFoundException(id: Long) : RuntimeException("Car check-up ID $id not found")
