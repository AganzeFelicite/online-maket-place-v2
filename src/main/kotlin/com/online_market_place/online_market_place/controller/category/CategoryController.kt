package com.online_market_place.online_market_place.controller.category
import com.online_market_place.online_market_place.dto.category.CategoryResponse
import com.online_market_place.online_market_place.dto.category.CreateCategoryRequest
import com.online_market_place.online_market_place.dto.category.UpdateCategoryRequest
import com.online_market_place.online_market_place.service.service_interface.CategoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpStatus

@RestController
@RequestMapping("/api/v2.0/categories")
class CategoryController(
    private val categoryService: CategoryService
) {

    @GetMapping
    fun getAllCategories(): ResponseEntity<List<CategoryResponse>> {
        val categories = categoryService.getAllCategories()
        return ResponseEntity.ok(categories)
    }

    @GetMapping("/{id}")
    fun getCategoryById(@PathVariable id: Long): ResponseEntity<CategoryResponse> {
        val category = categoryService.getCategoryById(id)
        return ResponseEntity.ok(category)
    }

    @PostMapping
    fun createCategory(@RequestBody request: CreateCategoryRequest): ResponseEntity<CategoryResponse> {
        val createdCategory = categoryService.createCategory(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory)
    }

    @PutMapping
    fun updateCategory(@RequestBody request: UpdateCategoryRequest): ResponseEntity<CategoryResponse> {
        val updatedCategory = categoryService.updateCategory(request)
        return ResponseEntity.ok(updatedCategory)
    }

    @DeleteMapping("/{id}")
    fun deleteCategory(@PathVariable id: Long): ResponseEntity<String> {
        val message = categoryService.deleteCategory(id)
        return ResponseEntity.ok(message)
    }
}
