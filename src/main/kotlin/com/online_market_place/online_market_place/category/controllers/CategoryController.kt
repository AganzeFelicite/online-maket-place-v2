package com.online_market_place.online_market_place.category.controllers

import com.online_market_place.online_market_place.category.dto.CreateCategoryDTO
import com.online_market_place.online_market_place.category.dto.UpdateCategoryDTO
import com.online_market_place.online_market_place.category.services.CategoryService
import com.online_market_place.online_market_place.common.ApiResponse
import com.online_market_place.online_market_place.common.annotations.IsAdminOrSeller
import com.online_market_place.online_market_place.common.annotations.IsAdminOrSellerOrCustomer
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2.0/categories")
@SecurityRequirement(name = "bearerAuth")
class CategoryController(
    private val categoryService: CategoryService
) {

    @GetMapping
    @Operation(summary = "Get all product categories")
    @IsAdminOrSellerOrCustomer
    fun getAllCategories(): ResponseEntity<List<CreateCategoryDTO.Output>> {
        val categories = categoryService.getAllCategories()
        return ResponseEntity.ok(categories)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product category by Id")
    @IsAdminOrSellerOrCustomer
    fun getCategoryById(@PathVariable id: Long): ResponseEntity<CreateCategoryDTO.Output> {
        val category = categoryService.getCategoryById(id)
        return ResponseEntity.ok(category)
    }

    @PostMapping
    @Operation(summary = "Create product category")
    @IsAdminOrSeller
    fun createCategory(@RequestBody request: CreateCategoryDTO.Input): ResponseEntity<CreateCategoryDTO.Output> {
        val createdCategory = categoryService.createCategory(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory)
    }

    @PutMapping
    @Operation(summary = "Update product category")
    @IsAdminOrSeller
    fun updateCategory(@RequestBody request: UpdateCategoryDTO.Input): ResponseEntity<UpdateCategoryDTO.Output> {
        val updatedCategory = categoryService.updateCategory(request)
        return ResponseEntity.ok(updatedCategory)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete product")
    @IsAdminOrSeller
    fun deleteCategory(@PathVariable id: Long): ResponseEntity<ApiResponse> {
        categoryService.deleteCategory(id)
        val response = ApiResponse(
            message = "Category with ID $id deleted successfully",
            success = true,
        )

        return ResponseEntity.ok( response )
    }
}
