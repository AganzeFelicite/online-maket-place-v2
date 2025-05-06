package com.online_market_place.online_market_place.common.config.security

import com.online_market_place.online_market_place.user.repositories.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email)


        return org.springframework.security.core.userdetails.User(
            user.email,
            user.password,
            user.enabled,
            true, true, true,
            user.roles.map { SimpleGrantedAuthority("ROLE_${it.name}") }

        )
    }
}
