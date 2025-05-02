package com.online_market_place.online_market_place.user.services

import com.online_market_place.online_market_place.auth.dto.BaseUserResponse
import com.online_market_place.online_market_place.order.dto.OrderCreateDTO
import com.online_market_place.online_market_place.user.dto.UserUpdateDTO
import com.online_market_place.online_market_place.user.entities.UserEntity

interface UserService {
    fun getAllUsers(): List<BaseUserResponse>
    fun getUserById(id: Long): UserEntity
    fun updateUser(request: UserUpdateDTO.Input): BaseUserResponse

    fun getUserOrders(): List<OrderCreateDTO.Output>

    fun getUserOrdersByStatus(status: String): List<OrderCreateDTO.Output>

    fun getUserOrdersByProductId(productId: Long): List<OrderCreateDTO.Output>
    fun getUserOrdersByUserId(userId: Long): List<OrderCreateDTO.Output>

    fun deleteUser(id: Long): String
}
