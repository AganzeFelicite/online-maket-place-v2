package com.online_market_place.online_market_place.notification.service.impl

import com.online_market_place.online_market_place.notification.service.EmailService
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailServiceImpl(
    private val mailSender: JavaMailSender,


    ) : EmailService {
    override fun sendVerificationEmail(email: String, token: String) {
        val message = SimpleMailMessage()
        val emailVerificationLink: String = "http://localhost:8082/api/v1/auth/verify-email?token=" + token
        message.setTo(email)
        message.subject = "Verify your email, from Online Market Place"
        message.text = (("<p>Hello,</p>"
                + "<p>Please verify your email by clicking the link below:</p>"
                + "<p><a href=\"" + emailVerificationLink) + "\">Verify Email please as we are !n demo please pick the token from the url and go verify from th!s end po!nt /api/v1/users/verify </a></p>"
                + "<p>If you did not request this, please ignore this email.</p>")
        mailSender.send(message)

    }

    override fun sendPasswordResetEmail(email: String, token: String): String {
        TODO("Not yet implemented")
    }

    override fun sendEmail(email: String, subject: String, body: String): String {
        TODO("Not yet implemented")
    }

    override fun sendEmailWithAttachment(email: String, subject: String, body: String, attachmentPath: String): String {
        TODO("Not yet implemented")
    }
}