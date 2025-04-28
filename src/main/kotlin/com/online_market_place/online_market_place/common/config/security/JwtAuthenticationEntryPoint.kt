package com.online_market_place.online_market_place.common.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component


@Component
@Suppress("unused")
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: org.springframework.security.core.AuthenticationException?
    ) {
        if (response != null) {
            response.contentType = MediaType.APPLICATION_JSON.toString()
        }
        if (response != null) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
        }

        val body = mapOf(
            "status" to HttpServletResponse.SC_UNAUTHORIZED,
            "error" to "Unauthorized",
            "message" to authException?.message,
            "path" to request?.servletPath
        )

        val mapper = ObjectMapper()
        mapper.writeValue(response?.outputStream, body)
    }

}
