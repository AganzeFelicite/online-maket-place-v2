package com.online_market_place.online_market_place.product.repositories

import com.online_market_place.online_market_place.product.entities.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional



@Repository
interface ProductRepository : JpaRepository<ProductEntity, Long> {


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM products", nativeQuery = true)
    fun deleteAllPhysically()

}