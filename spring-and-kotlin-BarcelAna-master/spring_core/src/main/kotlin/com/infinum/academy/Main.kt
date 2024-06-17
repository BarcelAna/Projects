package com.infinum.academy

import com.infinum.academy.configuration.YamlPropertySourceFactory
import com.infinum.academy.service.CarCheckUpSystem
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@ComponentScan
@PropertySource("classpath:properties.yml", factory = YamlPropertySourceFactory::class)
class ApplicationConfiguration

fun main() {
    val context: ApplicationContext = AnnotationConfigApplicationContext(ApplicationConfiguration::class.java)
    val service: CarCheckUpSystem = context.getBean(CarCheckUpSystem::class.java)

    service.addCheckUp("AAAA333444TTTLKO") // MINI Cooper
    service.addCheckUp("P0ZZZ99ZLS258179") // Porsche
    service.addCheckUp("LPOKKKKJJ1112223") // Porsche

    println(service.countCheckUps("MINI Cooper")) // 1
    println(service.countCheckUps("Porsche")) // 2

    println(service.isCheckUpNecessary("P0ZZZ99ZLS258179")) // false

    println(service.isCheckUpNecessary("LKOTZU67522WXY42")) // true
    service.addCheckUp("LKOTZU67522WXY42")
    println(service.isCheckUpNecessary("LKOTZU67522WXY42")) // false

    println(service.getCheckUps("LKOTZU67522WXY42").size) // 1

//  service.getCheckUps("AAAAAAAAAAAAAAAAA")    //throws exception
//  service.addCheckUp("AAAAAAAAAAAAAAAAA")     //throws exception
}
