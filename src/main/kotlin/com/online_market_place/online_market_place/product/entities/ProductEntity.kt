package com.online_market_place.online_market_place.product.entities

import com.online_market_place.online_market_place.category.entities.CategoryEntity
import com.online_market_place.online_market_place.common.base.BaseEntity
import com.online_market_place.online_market_place.review.entities.ReviewEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "products")
@SQLDelete(sql = "UPDATE products SET deleted_at = now() WHERE id=?")
@SQLRestriction("deleted_at is null")
data class ProductEntity(
    var name: String,

    var description: String,

    var price: Double,

    var stock: Int,

    @Column(name = "is_featured")
    var isFeatured: Boolean = false,

    var productImageUrl: String? = null,


    @ManyToOne
    @JoinColumn(name = "category_id")
    var category: CategoryEntity,

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL])
    val reviews: List<ReviewEntity> = mutableListOf()


) : BaseEntity() {
    fun updateStock(quantity: Int) {
        this.stock -= quantity
    }

    fun updateProductDetails(

        name: String? = null,
        description: String? = null,
        price: Double? = null,
        stock: Int? = null,
        isFeatured: Boolean? = null,
        productImageUrl: String? = null,
        category: CategoryEntity? = null

    ) {
        name?.let { this.name = it }
        description?.let { this.description = it }
        price?.let { this.price = it }
        stock?.let { this.stock = it }
        isFeatured?.let { this.isFeatured = it }
        productImageUrl?.let { this.productImageUrl = it }
        category?.let { this.category = it }
    }

}
