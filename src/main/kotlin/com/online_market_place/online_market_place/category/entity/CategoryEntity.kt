package com.online_market_place.online_market_place.category.entity

import com.online_market_place.online_market_place.product.entity.ProductEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDateTime

@Entity
@Table(name = "categories")

// TODO Base entity for all entities (id, createdAt, updatedAt, deletedAt)
// TODO Implement soft delete. i.e. When records are deleted - only set the deletedAt field but do not physically delete the record
@SQLDelete(sql = "UPDATE categories SET deleted_at = now() WHERE id=?")
@SQLRestriction("deleted_at is null")
data class CategoryEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val deletedAt: LocalDateTime? = null,

    var name: String,

    // TODO : You should not default to cascading
    @OneToMany(mappedBy = "category", cascade = [CascadeType.ALL])
    val products: List<ProductEntity> = mutableListOf()
)
