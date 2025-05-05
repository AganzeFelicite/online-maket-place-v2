package com.online_market_place.online_market_place.notification.services

import com.online_market_place.online_market_place.notification.dto.EmailMessage

interface EmailService {
    fun sendVerificationEmail(email: String, token: String)

    fun publishEmailNotificationEvent(message: EmailMessage)
}