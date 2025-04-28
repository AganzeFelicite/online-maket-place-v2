package com.online_market_place.online_market_place.common.config.security

import com.online_market_place.online_market_place.auth.service.TokenBlacklistService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
@RequiredArgsConstructor
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val userDetailsService: CustomUserDetailsService,
    private val tokenBlacklistService: TokenBlacklistService,
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)

            if (tokenBlacklistService.isBlacklisted(token)) {
                SecurityContextHolder.clearContext()
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                return
            }
            val email = jwtUtil.extractEmail(token)

            if (SecurityContextHolder.getContext().authentication == null) {
                val userDetails = userDetailsService.loadUserByUsername(email)
                if (jwtUtil.validateToken(token, userDetails)) {
                    val auth = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                    SecurityContextHolder.getContext().authentication = auth
                }
            }
        }
        chain.doFilter(request, response)
    }
}
