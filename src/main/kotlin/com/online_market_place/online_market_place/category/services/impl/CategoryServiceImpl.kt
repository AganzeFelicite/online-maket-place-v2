package com.online_market_place.online_market_place.category.services.impl

import com.online_market_place.online_market_place.category.dto.CreateCategoryDTO
import com.online_market_place.online_market_place.category.dto.UpdateCategoryDTO
import com.online_market_place.online_market_place.category.mappers.CategoryMapper
import com.online_market_place.online_market_place.category.repositories.CategoryRepository
import com.online_market_place.online_market_place.category.services.CategoryService
import com.online_market_place.online_market_place.common.exceptions.ResourceNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository,
) : CategoryService {
    override fun getAllCategories(): List<CreateCategoryDTO.Output> {
        val categories = categoryRepository.findAll()
        return categories.map { CategoryMapper().map(it) }
    }

    override fun getCategoryById(categoryId: Long): CreateCategoryDTO.Output {
        val category = categoryRepository.findById(categoryId)
            .orElseThrow { ResourceNotFoundException("Category with ID $categoryId not found") }

        return CategoryMapper().map(category)
    }


    override fun createCategory(category: CreateCategoryDTO.Input): CreateCategoryDTO.Output {
        val existingCategory = categoryRepository.findByName(category.name.trim())
        if (existingCategory.isNotEmpty()) {
            throw ResourceNotFoundException("Category with name '${category.name}' already exists")
        }

        val newCategory = CategoryMapper().map(category)
        return try {
            val savedCategory = categoryRepository.save(newCategory)
            CategoryMapper().map(savedCategory)
        } catch (e: Exception) {
            throw RuntimeException("Failed to create category: ${e.message}")
        }
    }


    @Transactional
    override fun updateCategory(updateCategoryRequest: UpdateCategoryDTO.Input): UpdateCategoryDTO.Output {
        val existingCategory = categoryRepository.findById(updateCategoryRequest.id)
            .orElseThrow { ResourceNotFoundException("Category with ID ${updateCategoryRequest.id} not found") }

        val categoryWithSameName = categoryRepository.findByName(updateCategoryRequest.name.trim())
        if (categoryWithSameName.isNotEmpty() && categoryWithSameName[0].id != existingCategory.id) {
            throw ResourceNotFoundException("Category with name '${updateCategoryRequest.name}' already exists")
        }

        existingCategory.updateName(updateCategoryRequest.name.trim())

        return try {
            val updatedCategory = categoryRepository.save(existingCategory)
            CategoryMapper().mapToUpdateResponse(updatedCategory)
        } catch (e: Exception) {
            throw RuntimeException("Failed to update category: ${e.message}")
        }
    }


    override fun deleteCategory(categoryId: Long) {
        val category = categoryRepository.findById(categoryId)
            .orElseThrow { ResourceNotFoundException("Category with ID $categoryId not found") }



        categoryRepository.delete(category)


    }

}