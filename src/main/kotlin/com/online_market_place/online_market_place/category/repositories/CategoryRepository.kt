package com.online_market_place.online_market_place.category.repositories

import com.online_market_place.online_market_place.category.entities.CategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<CategoryEntity, Long> {
    fun findByName(categoryName: String): List<CategoryEntity>
}