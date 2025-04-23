package com.online_market_place.online_market_place.repository.product

import com.online_market_place.online_market_place.entiy.category.CategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductCategoryRepository : JpaRepository<CategoryEntity, Long> {
    fun findByName(categoryName: String): CategoryEntity?
}