package academy.hw01.car_checkup

import java.time.LocalDateTime

object CarCheckUpSystem {
    var cars = mutableListOf<Car>()
    var checkUps = mutableListOf<CarCheckUp>()

    init {
        cars.add(Car("Porsche", "Taycan", "P0ZZZ99ZLS258179"))
        cars.add(Car("MINI Cooper", "Cooper SD", "AAAA333444TTTLKO"))
        cars.add(Car("Porsche", "Taycan", "LPOKKKKJJ1112223"))
        cars.add(Car("MINI Cooper", "Cooper SD", "LKOTZU67522WXY42"))

        checkUps.add(CarCheckUp(LocalDateTime.now(), cars[0]))
        checkUps.add(CarCheckUp(LocalDateTime.now(), cars[1]))
        checkUps.add(CarCheckUp(LocalDateTime.now().minusYears(2), cars[3]))
        checkUps.add(CarCheckUp(LocalDateTime.now().minusYears(3), cars[1]))
        checkUps.add(CarCheckUp(LocalDateTime.now(), cars[0]))
        checkUps.add(CarCheckUp(LocalDateTime.now(), cars[2]))
        checkUps.add(CarCheckUp(LocalDateTime.now(), cars[2]))
        checkUps.add(CarCheckUp(LocalDateTime.now(), cars[2]))
        checkUps.add(CarCheckUp(LocalDateTime.now().minusYears(4), cars[1]))
        checkUps.add(CarCheckUp(LocalDateTime.now().minusYears(3), cars[3]))
    }

    fun isCheckUpNecessary(vin: String) = checkUps.none { chUp -> chUp.car.vin == vin && chUp.performedAt.isAfter(LocalDateTime.now().minusYears(1)) }

    fun addCheckUp(vin: String): CarCheckUp {
        val car = cars.find { c -> c.vin==vin } ?: throw NoSuchElementException("Car with the given vin does not exist")
        val checkUp = CarCheckUp(LocalDateTime.now(), car)
        checkUps.add(checkUp)
        return checkUp
    }

    fun getCheckUps(vin: String) : List<CarCheckUp> {
        val checkUps = checkUps.filter {chUp -> chUp.car.vin==vin}
        if (checkUps.isEmpty()) {
            throw NoSuchElementException("Car with the given vin does not exist")
        }
        return checkUps
    }

    fun countCheckUps(manufacturer: String) = checkUps.count { chUp -> chUp.car.manufacturer == manufacturer }
}

fun main() {
    println(CarCheckUpSystem.isCheckUpNecessary("P0ZZZ99ZLS258179")) //false
    println(CarCheckUpSystem.isCheckUpNecessary("LKOTZU67522WXY42")) //true
    println(CarCheckUpSystem.isCheckUpNecessary("AAAA333444TTTLKO")) //false

    CarCheckUpSystem.addCheckUp("LKOTZU67522WXY42")
    println(CarCheckUpSystem.isCheckUpNecessary("LKOTZU67522WXY42")) //false

//  CarCheckUpSystem.addCheckUp("AAAAAAAAAAAAAAAA")  //throws NoSuchElementException

    val checkUps = CarCheckUpSystem.getCheckUps("LKOTZU67522WXY42")
    println(checkUps.size) //3

//  CarCheckUpSystem.getCheckUps("AAAAAAAAAAAAAAAA")  //throws NoSuchElementException

    println(CarCheckUpSystem.countCheckUps("Porsche")) //5
    println(CarCheckUpSystem.countCheckUps("MINI Cooper")) //6
}