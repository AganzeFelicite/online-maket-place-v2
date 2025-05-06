package com.online_market_place.online_market_place.common.config.rabbitmq

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@Suppress("unused")
class RabbitMQConfig {

    companion object {
        const val QUEUE_ORDERS = "orders-queue"
        const val EXCHANGE_ORDERS = "orders-exchange"
        const val ROUTING_KEY_ORDERS = "orders.created"

        const val QUEUE_ORDER_STATUS = "order-status-queue"
        const val ROUTING_KEY_ORDER_STATUS = "orders.status.updated"

        const val QUEUE_EMAIL = "email-queue"
        const val ROUTING_KEY_EMAIL = "notification.email"
    }

    @Bean(name = ["ordersQueue"])
    fun ordersQueue(): Queue = Queue(QUEUE_ORDERS, true)

    @Bean(name = ["orderStatusQueue"])
    fun orderStatusQueue(): Queue = Queue(QUEUE_ORDER_STATUS, true)

    @Bean(name = ["emailQueue"])
    fun emailQueue(): Queue = Queue(QUEUE_EMAIL, true)

    @Bean
    fun ordersExchange(): TopicExchange = TopicExchange(EXCHANGE_ORDERS)

    @Bean
    fun ordersBinding(ordersQueue: Queue, ordersExchange: TopicExchange): Binding =
        BindingBuilder.bind(ordersQueue).to(ordersExchange).with(ROUTING_KEY_ORDERS)

    @Bean
    fun orderStatusBinding(orderStatusQueue: Queue, ordersExchange: TopicExchange): Binding =
        BindingBuilder.bind(orderStatusQueue).to(ordersExchange).with(ROUTING_KEY_ORDER_STATUS)

    @Bean
    fun emailBinding(emailQueue: Queue, ordersExchange: TopicExchange): Binding =
        BindingBuilder.bind(emailQueue).to(ordersExchange).with(ROUTING_KEY_EMAIL)


    @Bean
    fun messageConverter(): Jackson2JsonMessageConverter = Jackson2JsonMessageConverter()

    @Bean
    fun amqpTemplate(connectionFactory: ConnectionFactory): RabbitTemplate =
        RabbitTemplate(connectionFactory).apply {
            messageConverter = messageConverter()
        }
}
