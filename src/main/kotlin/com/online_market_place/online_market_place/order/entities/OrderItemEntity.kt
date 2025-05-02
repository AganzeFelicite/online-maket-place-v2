package com.online_market_place.online_market_place.order.entities

import com.online_market_place.online_market_place.common.base.BaseEntity
import com.online_market_place.online_market_place.product.entities.ProductEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction


@Entity
@Table(name = "order_items")
@SQLDelete(sql = "UPDATE order_items SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at is null")
data class OrderItemEntity(
    @ManyToOne
    @JoinColumn(name = "order_id")
    var order: OrderEntity? = null,


    @ManyToOne
    @JoinColumn(name = "product_id")
    val product: ProductEntity,

    var quantity: Int,

    var price: Double
) : BaseEntity() {
    fun updateQuantity(newQuantity: Int) {
        this.quantity = newQuantity
    }

    fun updatePrice(newPrice: Double) {
        this.price = newPrice
    }
}