package com.online_market_place.online_market_place.category.entities

import com.online_market_place.online_market_place.common.base.BaseEntity
import com.online_market_place.online_market_place.product.entities.ProductEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction


@Entity
@Table(name = "categories")
@SQLDelete(sql = "UPDATE categories SET deleted_at = now() WHERE id=?")
@SQLRestriction("deleted_at is null")
data class CategoryEntity(
    var name: String,

    @OneToMany(mappedBy = "category")
    val products: List<ProductEntity> = mutableListOf()
) : BaseEntity() {
    fun updateName(newName: String) {
        this.name = newName
    }
}
