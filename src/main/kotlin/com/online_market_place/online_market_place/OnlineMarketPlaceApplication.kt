package com.online_market_place.online_market_place

import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling


@SpringBootApplication
@EnableRabbit
@EnableScheduling
class OnlineMarketPlaceApplication

fun main(args: Array<String>) {
	runApplication<OnlineMarketPlaceApplication>(*args)
}
