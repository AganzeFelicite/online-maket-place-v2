package com.online_market_place.online_market_place.user.mapper

import com.online_market_place.online_market_place.auth.dto.BaseUserResponse
import com.online_market_place.online_market_place.auth.dto.UserDetailedResponse
import com.online_market_place.online_market_place.auth.dto.UserResponse
import com.online_market_place.online_market_place.order.mapper.toOrderResponse
import com.online_market_place.online_market_place.review.mapper.toReviewResponse
import com.online_market_place.online_market_place.user.entity.UserEntity
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


