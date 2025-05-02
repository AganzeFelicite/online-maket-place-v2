package com.online_market_place.online_market_place.category.services

import com.online_market_place.online_market_place.category.dto.CreateCategoryDTO
import com.online_market_place.online_market_place.category.dto.UpdateCategoryDTO

interface CategoryService {
    fun getAllCategories(): List<CreateCategoryDTO.Output>

    fun getCategoryById(categoryId: Long): CreateCategoryDTO.Output

    fun createCategory(category: CreateCategoryDTO.Input): CreateCategoryDTO.Output

    fun updateCategory(updateCategoryRequest: UpdateCategoryDTO.Input): UpdateCategoryDTO.Output

    fun deleteCategory(categoryId: Long)
}