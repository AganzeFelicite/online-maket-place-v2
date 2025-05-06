package com.online_market_place.online_market_place.user.services.impl

import com.online_market_place.online_market_place.auth.dto.BaseUserResponse
import com.online_market_place.online_market_place.common.exceptions.ResourceNotFoundException
import com.online_market_place.online_market_place.order.dto.OrderCreateDTO
import com.online_market_place.online_market_place.order.mappers.OrderMapper
import com.online_market_place.online_market_place.user.dto.UserUpdateDTO
import com.online_market_place.online_market_place.user.entities.UserEntity
import com.online_market_place.online_market_place.user.mappers.UserMapper
import com.online_market_place.online_market_place.user.repositories.UserRepository
import com.online_market_place.online_market_place.user.services.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    override fun getAllUsers(): List<BaseUserResponse> {

        return userRepository.findAll().map { UserMapper().map(it) }
    }

    override fun getUserById(id: Long): BaseUserResponse {
        val user = userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User with id $id not found") }
        return UserMapper().map(user)
    }

    @Transactional
    override fun updateUser(request: UserUpdateDTO.Input): BaseUserResponse {
        val currentUser = getAuthenticatedUser()

        request.username?.let { currentUser.username = it }
        request.password?.let { currentUser.password = passwordEncoder.encode(it) }
        request.enabled?.let { currentUser.enabled = it }
        request.role?.let { currentUser.roles = it }

        return UserMapper().map(userRepository.save(currentUser))
    }

    override fun getUserOrders(): List<OrderCreateDTO.Output> {
        val user = getAuthenticatedUser()
        return user.orders.map { OrderMapper().map(it) }
    }

    override fun getUserOrdersByStatus(status: String): List<OrderCreateDTO.Output> {
        val user = getAuthenticatedUser()
        return user.orders.filter { it.status.name == status.uppercase() }.map { OrderMapper().map(it) }
    }

    override fun getUserOrdersByProductId(productId: Long): List<OrderCreateDTO.Output> {
        val user = getAuthenticatedUser()
        return user.orders.filter { order ->
            order.items.any { it.product.id == productId }
        }.map { OrderMapper().map(it) }
    }

    override fun getUserOrdersByUserId(userId: Long): List<OrderCreateDTO.Output> {
        val user = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("User with id $userId not found") }
        return user.orders.map { OrderMapper().map(it) }
    }

    override fun deleteUser(id: Long): String {
        val user = userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User with id $id not found") }
        userRepository.delete(user)
        return "User with id $id deleted successfully."
    }

    private fun getAuthenticatedUser(): UserEntity {
        val email = org.springframework.security.core.context.SecurityContextHolder.getContext().authentication.name
            ?: throw ResourceNotFoundException("Authenticated user not found")
        return userRepository.findByEmail(email)

    }
}
