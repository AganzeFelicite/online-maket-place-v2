package com.online_market_place.online_market_place.common.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.online_market_place.online_market_place.common.exception.model.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.web.access.AccessDeniedHandler

import java.time.LocalDateTime

class CustomAccessDeniedHandler : AccessDeniedHandler {
    override fun handle(request: HttpServletRequest, response: HttpServletResponse, accessDeniedException: org.springframework.security.access.AccessDeniedException) {
        response.contentType = "application/json"
        response.status = HttpServletResponse.SC_FORBIDDEN

        val errorResponse = ErrorResponse(
            status = HttpStatus.FORBIDDEN.value(),
            message = accessDeniedException.message ?: "Access denied",
            errorCode = "FORBIDDEN",
            timestamp = LocalDateTime.now(),
            path = request.requestURI
        )

        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        response.outputStream.println(mapper.writeValueAsString(errorResponse))
    }
}