package com.online_market_place.online_market_place.common.config.security
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class CustomAccessDeniedHandler : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        accessDeniedException: org.springframework.security.access.AccessDeniedException?
    ) {
        // Don't throw a new exception, handle it directly
        response?.status = HttpServletResponse.SC_FORBIDDEN
        response?.contentType = "application/json"

        val errorJson = """
            {
                "status": 403,
                "error": "Forbidden",
                "message": "You do not have permission to access this resource.",
                "path": "${request?.requestURI ?: ""}"
            }
        """.trimIndent()

        response?.writer?.write(errorJson)
    }
}