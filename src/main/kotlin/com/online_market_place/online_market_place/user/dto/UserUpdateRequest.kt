package com.online_market_place.online_market_place.user.dto

import com.online_market_place.online_market_place.user.enum_.UserRole

data class UserUpdateRequest(
    val id: Long,
    val username: String? = null,
    val password: String? = null,
    val enabled: Boolean? = null,
    val role: Set<UserRole>? = null,
    )