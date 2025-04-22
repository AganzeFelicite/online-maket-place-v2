package com.online_market_place.online_market_place.service.service_interface

interface EmailService {
    fun sendVerificationEmail(email: String, token: String)
    fun sendPasswordResetEmail(email: String, token: String): String
    fun sendEmail(email: String, subject: String, body: String): String
    fun sendEmailWithAttachment(email: String, subject: String, body: String, attachmentPath: String): String
}