package com.online_market_place.online_market_place.category.repositories

import com.online_market_place.online_market_place.category.entities.CategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface CategoryRepository : JpaRepository<CategoryEntity, Long> {
    fun findByName(categoryName: String): List<CategoryEntity>

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM categories", nativeQuery = true)
    fun deleteAllPhysically()
}