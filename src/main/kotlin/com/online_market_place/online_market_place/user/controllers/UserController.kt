package com.online_market_place.online_market_place.user.controllers

import com.online_market_place.online_market_place.auth.dto.BaseUserResponse
import com.online_market_place.online_market_place.common.ApiResponse
import com.online_market_place.online_market_place.common.annotations.IsAdmin

import com.online_market_place.online_market_place.common.annotations.IsAdminOrSellerOrCustomer
import com.online_market_place.online_market_place.user.dto.UserUpdateDTO
import com.online_market_place.online_market_place.user.services.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2.0/users")
@SecurityRequirement(name = "bearerAuth")
@Suppress("unused") // Suppress unused warning for this controller
class UserController(
    private val userService: UserService
) {

    @GetMapping
    @IsAdmin
    @Operation(summary = "Get all users")
    fun getAllUsers(): ResponseEntity<List<BaseUserResponse>> {
        return ResponseEntity.ok(userService.getAllUsers())
    }


    @GetMapping("/{id}")
    @IsAdminOrSellerOrCustomer
    @Operation(summary = "Get user by ID")
    fun getUserById(@PathVariable id: Long): ResponseEntity<BaseUserResponse> {
        return ResponseEntity.ok(userService.getUserById(id))
    }

    @PutMapping
    @IsAdminOrSellerOrCustomer
    @Operation(summary = "Update user")
    fun updateUser(@RequestBody request: UserUpdateDTO): ResponseEntity<BaseUserResponse> {
        return ResponseEntity.ok(userService.updateUser(request))
    }

    @GetMapping("/orders")
    @IsAdminOrSellerOrCustomer
    @Operation(summary = "Get all user orders")
    fun getUserOrders(): ResponseEntity<List<OrderResponse>> {
        return ResponseEntity.ok(userService.getUserOrders())
    }

    @GetMapping("/orders/status/{status}")
    @IsAdminOrSellerOrCustomer
    @Operation(summary = "Get user orders by status")
    fun getUserOrdersByStatus(@PathVariable status: String): ResponseEntity<List<OrderResponse>> {
        return ResponseEntity.ok(userService.getUserOrdersByStatus(status))
    }


    @GetMapping("/{userId}/orders")
    @IsAdminOrSellerOrCustomer
    @Operation(summary = "Get user orders by user ID")
    fun getUserOrdersByUserId(@PathVariable userId: Long): ResponseEntity<List<OrderResponse>> {
        return ResponseEntity.ok(userService.getUserOrdersByUserId(userId))
    }

    @DeleteMapping("/{id}")
    @IsAdmin
    @Operation(summary = "Delete user")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<ApiResponse> {
        val response = ApiResponse(
            message = userService.deleteUser(id),
            success = true
        )
        return ResponseEntity.ok(response)
    }
}
