package com.online_market_place.online_market_place.category.controller

import com.online_market_place.online_market_place.category.dto.CategoryResponse
import com.online_market_place.online_market_place.category.dto.CreateCategoryRequest
import com.online_market_place.online_market_place.category.dto.UpdateCategoryRequest
import com.online_market_place.online_market_place.category.service.CategoryService
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
    fun getAllCategories(): ResponseEntity<List<CategoryResponse>> {
        val categories = categoryService.getAllCategories()
        return ResponseEntity.ok(categories)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product category by Id")
    fun getCategoryById(@PathVariable id: Long): ResponseEntity<CategoryResponse> {
        val category = categoryService.getCategoryById(id)
        return ResponseEntity.ok(category)
    }

    @PostMapping
    @Operation(summary = "Create product category")
    fun createCategory(@RequestBody request: CreateCategoryRequest): ResponseEntity<CategoryResponse> {
        val createdCategory = categoryService.createCategory(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory)
    }

    @PutMapping
    @Operation(summary = "Update product category")
    fun updateCategory(@RequestBody request: UpdateCategoryRequest): ResponseEntity<CategoryResponse> {
        val updatedCategory = categoryService.updateCategory(request)
        return ResponseEntity.ok(updatedCategory)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete product")
    fun deleteCategory(@PathVariable id: Long): ResponseEntity<String> {
        val message = categoryService.deleteCategory(id)
        return ResponseEntity.ok(message)
    }
}
