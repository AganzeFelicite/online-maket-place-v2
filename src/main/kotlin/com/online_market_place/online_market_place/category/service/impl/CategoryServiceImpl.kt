package com.online_market_place.online_market_place.category.service.impl

import com.online_market_place.online_market_place.category.dto.CategoryResponse
import com.online_market_place.online_market_place.category.dto.CreateCategoryRequest
import com.online_market_place.online_market_place.category.dto.ProductCategoryRepository
import com.online_market_place.online_market_place.category.dto.UpdateCategoryRequest
import com.online_market_place.online_market_place.category.mapper.toCategoryResponse
import com.online_market_place.online_market_place.category.mapper.toEntity
import com.online_market_place.online_market_place.category.service.CategoryService
import com.online_market_place.online_market_place.common.exception.ResourceNotFoundException
import com.online_market_place.online_market_place.product.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(
    private val categoryRepository: ProductCategoryRepository,
    private val productRepository: ProductRepository,

    ) : CategoryService {
    override fun getAllCategories(): List<CategoryResponse> {
        val categories = categoryRepository.findAll()

        if (categories.isEmpty()) {
            throw ResourceNotFoundException("No categories found")
        }

        return categories.map { it.toCategoryResponse() }
    }

    override fun getCategoryById(categoryId: Long): CategoryResponse {
        val category = categoryRepository.findById(categoryId)
            .orElseThrow { ResourceNotFoundException("Category with ID $categoryId not found") }

        return category.toCategoryResponse()
    }


    override fun createCategory(category: CreateCategoryRequest): CategoryResponse {

        // Check for existing category (case insensitive)
        val existingCategory = categoryRepository.findByName(category.name.trim())
        if (existingCategory != null) {
            throw ResourceNotFoundException("Category with name '${category.name}' already exists")
        }

        return try {
            val savedCategory = categoryRepository.save(category.toEntity())
            savedCategory.toCategoryResponse()
        } catch (e: Exception) {
            throw RuntimeException("Failed to create category: ${e.message}")
        }
    }


    override fun updateCategory(updateCategoryRequest: UpdateCategoryRequest): CategoryResponse {
        val existingCategory = categoryRepository.findById(updateCategoryRequest.id)
            .orElseThrow { ResourceNotFoundException("Category with ID $updateCategoryRequest.id not found") }

        // Check if another category with the same name already exists (case-insensitive)
        val categoryWithSameName = categoryRepository.findByName(updateCategoryRequest.name.trim())
        if (categoryWithSameName != null && categoryWithSameName.id != updateCategoryRequest.id) {
            throw ResourceNotFoundException("Another category with name '${updateCategoryRequest.name}' already exists")
        }

        existingCategory.name = updateCategoryRequest.name.trim()

        return try {
            val updatedCategory = categoryRepository.save(existingCategory)
            updatedCategory.toCategoryResponse()
        } catch (e: Exception) {
            throw RuntimeException("Failed to update category: ${e.message}")
        }
    }


    override fun deleteCategory(categoryId: Long): String {
        val category = categoryRepository.findById(categoryId)
            .orElseThrow { ResourceNotFoundException("Category with ID $categoryId not found") }

        // Check if the category has any products
        val products = productRepository.findAllByCategory(category)
        if (products != null) {
            if (products.isNotEmpty()) {
                throw ResourceNotFoundException("Cannot delete category with ID $categoryId because it has associated products")
            }
        }

        categoryRepository.delete(category)

        return "Category with ID $categoryId deleted successfully"
    }

}