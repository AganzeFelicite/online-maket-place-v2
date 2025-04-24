package com.online_market_place.online_market_place

import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories


@SpringBootApplication
@EnableRabbit
// TODO Feedback: Don't think you need this annotation
@EnableJpaRepositories(basePackages = ["com.online_market_place.online_market_place.repository"])

class OnlineMaketPlaceApplication

fun main(args: Array<String>) {
	runApplication<OnlineMaketPlaceApplication>(*args)
}
