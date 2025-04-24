package com.online_market_place.online_market_place.category.service

import com.online_market_place.online_market_place.category.dto.CategoryResponse
import com.online_market_place.online_market_place.category.dto.CreateCategoryRequest
import com.online_market_place.online_market_place.category.dto.UpdateCategoryRequest

interface CategoryService {
    fun getAllCategories(): List<CategoryResponse>

    fun getCategoryById(categoryId: Long): CategoryResponse

    fun createCategory(category: CreateCategoryRequest): CategoryResponse

    fun updateCategory(updateCategoryRequest: UpdateCategoryRequest): CategoryResponse

    fun deleteCategory(categoryId: Long): String
}