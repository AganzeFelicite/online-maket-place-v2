package com.online_market_place.online_market_place.user.service.impl

import BaseUserResponse
import com.online_market_place.online_market_place.common.exception.ResourceNotFoundException
import com.online_market_place.online_market_place.order.dto.OrderResponse
import com.online_market_place.online_market_place.order.mapper.toOrderResponse
import com.online_market_place.online_market_place.user.dto.UserUpdateRequest
import com.online_market_place.online_market_place.user.mapper.toUserResponse
import com.online_market_place.online_market_place.user.repository.UserRepository
import com.online_market_place.online_market_place.user.service.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    override fun getAllUsers(): List<BaseUserResponse> {

        return userRepository.findAll().map { it.toUserResponse() }
    }

    override fun getUserById(id: Long): BaseUserResponse {
        val user = userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User with id $id not found") }
        return user.toUserResponse()
    }

    @Transactional
    override fun updateUser(request: UserUpdateRequest): BaseUserResponse {
        val currentUser = getAuthenticatedUser()

        request.username?.let { currentUser.username = it }
        request.password?.let { currentUser.password = passwordEncoder.encode(it) }
        request.enabled?.let { currentUser.enabled = it }
        request.role?.let { currentUser.role = it }

        return userRepository.save(currentUser).toUserResponse()
    }

    override fun getUserOrders(): List<OrderResponse> {
        val user = getAuthenticatedUser()
        return user.orders?.map { it.toOrderResponse() } ?: emptyList()
    }

    override fun getUserOrdersByStatus(status: String): List<OrderResponse> {
        val user = getAuthenticatedUser()
        return user.orders?.filter { it.status.name == status.uppercase() }?.map { it.toOrderResponse() } ?: emptyList()
    }

    override fun getUserOrdersByProductId(productId: Long): List<OrderResponse> {
        val user = getAuthenticatedUser()
        return user.orders?.filter { order ->
            order.items.any { it.product.id == productId }
        }?.map { it.toOrderResponse() } ?: emptyList()
    }

    override fun getUserOrdersByUserId(userId: Long): List<OrderResponse> {
        val user = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("User with id $userId not found") }
        return user.orders?.map { it.toOrderResponse() } ?: emptyList()
    }

    override fun deleteUser(id: Long): String {
        val user = userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User with id $id not found") }
        userRepository.delete(user)
        return "User with id $id deleted successfully."
    }

    private fun getAuthenticatedUser(): com.online_market_place.online_market_place.user.entity.UserEntity {
        val email = org.springframework.security.core.context.SecurityContextHolder.getContext().authentication.name
            ?: throw ResourceNotFoundException("Authenticated user not found")
        return userRepository.findByEmail(email)
            ?: throw ResourceNotFoundException("Authenticated user not found")
    }
}
