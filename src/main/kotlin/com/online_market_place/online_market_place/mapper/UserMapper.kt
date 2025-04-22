package com.online_market_place.online_market_place.mapper

import BaseUserResponse
import UserDetailedResponse
import UserResponse
import com.online_market_place.online_market_place.entiy.user.UserEntity
import java.time.LocalDateTime

fun UserEntity.toUserResponse(withDetails: Boolean = false): BaseUserResponse {
    return if (!withDetails) {
        UserResponse(
            id = id ?: 0L,
            email = email,
            username = username,
            role = role
        )
    } else {
        UserDetailedResponse(
            id = id ?: 0L,
            email = email,
            username = username,
            role = role,
            orders = orders?.map { it.toOrderResponse() } ?: emptyList(),
            reviews = reviews?.map { it.toReviewResponse() } ?: emptyList(),
            createdAt = createAt ?: LocalDateTime.now(),
            updatedAt = update ?: LocalDateTime.now()
        )
    }
}


