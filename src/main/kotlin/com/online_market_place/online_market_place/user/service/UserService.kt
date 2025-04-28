package com.online_market_place.online_market_place.user.service

import com.online_market_place.online_market_place.auth.dto.BaseUserResponse
import com.online_market_place.online_market_place.order.dto.OrderResponse
import com.online_market_place.online_market_place.user.dto.UserUpdateRequest

interface UserService {
    fun getAllUsers(): List<BaseUserResponse>
    fun getUserById(id: Long): BaseUserResponse
    fun updateUser(request: UserUpdateRequest): BaseUserResponse

    fun getUserOrders(): List<OrderResponse>

    fun getUserOrdersByStatus(status: String): List<OrderResponse>

    fun getUserOrdersByProductId(productId: Long): List<OrderResponse>
    fun getUserOrdersByUserId(userId: Long): List<OrderResponse>

    fun deleteUser(id: Long): String
}
