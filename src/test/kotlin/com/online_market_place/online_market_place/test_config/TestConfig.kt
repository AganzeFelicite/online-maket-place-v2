package com.online_market_place.online_market_place.test_config

import com.online_market_place.online_market_place.notification.dto.EmailMessage
import com.online_market_place.online_market_place.notification.services.EmailService
import com.online_market_place.online_market_place.order.dto.OrderMessage
import com.online_market_place.online_market_place.order.services.OrderProducer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

class TestConfig {
    @Bean
    @Primary
    fun emailService(): EmailService {
        return object : EmailService {
            override fun sendVerificationEmail(to: String, token: String) {
                // Do nothing
            }

            override fun publishEmailNotificationEvent(message: EmailMessage) {

            }


        }
    }

    @Bean
    @Primary
    fun messageProducer(): OrderProducer {
        return object : OrderProducer {


            override fun publishOrderCreatedEvent(orderMessage: OrderMessage) {

            }

            override fun publishOrderStatusUpdateEvent(orderMessage: OrderMessage) {

            }
        }
    }

}