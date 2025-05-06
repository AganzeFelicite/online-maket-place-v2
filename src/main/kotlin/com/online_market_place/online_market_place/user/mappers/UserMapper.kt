package com.online_market_place.online_market_place.user.mappers

import com.online_market_place.online_market_place.auth.dto.BaseUserResponse
import com.online_market_place.online_market_place.auth.dto.UserDetailedResponse
import com.online_market_place.online_market_place.auth.dto.UserResponse
import com.online_market_place.online_market_place.order.mappers.OrderMapper
import com.online_market_place.online_market_place.review.mappers.toReviewResponse
import com.online_market_place.online_market_place.user.entities.UserEntity


class UserMapper {

    fun map(userEntity: UserEntity, withDetails: Boolean = false): BaseUserResponse {
        return if (!withDetails) {
            UserResponse(
                id = userEntity.id,
                email = userEntity.email,
                username = userEntity.username,
                role = userEntity.roles
            )
        } else {
            UserDetailedResponse(
                id = userEntity.id,
                email = userEntity.email,
                username = userEntity.username,
                role = userEntity.roles,
                orders = userEntity.orders.map { OrderMapper().map(it) },
                reviews = userEntity.reviews?.map { it.toReviewResponse() } ?: emptyList(),
                createdAt = userEntity.createdAt,
                updatedAt = userEntity.updatedAt
            )
        }
    }

    fun map(userEntities: List<UserEntity>, withDetails: Boolean = false): List<BaseUserResponse> {
        return userEntities.map { map(it, withDetails) }
    }
}
