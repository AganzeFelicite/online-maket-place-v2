package com.online_market_place.online_market_place.service.service_interface

import com.online_market_place.online_market_place.dto.category.CategoryResponse
import com.online_market_place.online_market_place.dto.category.CreateCategoryRequest
import com.online_market_place.online_market_place.dto.category.UpdateCategoryRequest

interface CategoryService {
    fun getAllCategories(): List<CategoryResponse>

    fun getCategoryById(categoryId: Long): CategoryResponse

    fun createCategory(category: CreateCategoryRequest): CategoryResponse

    fun updateCategory(updateCategoryRequest: UpdateCategoryRequest): CategoryResponse

    fun deleteCategory(categoryId: Long): String
}