package com.online_market_place.online_market_place.order.entities

import com.online_market_place.online_market_place.common.base.BaseEntity
import com.online_market_place.online_market_place.product.enums.ProductOrderStatus
import com.online_market_place.online_market_place.user.entities.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction


@Entity
@Table(name = "orders")
@SQLDelete(sql = "UPDATE orders SET deleted_at = now() WHERE id=?")
@SQLRestriction("deleted_at is null")
data class OrderEntity(
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: UserEntity,

    var status: ProductOrderStatus = ProductOrderStatus.PENDING,


    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL])
    val items: List<OrderItemEntity
            > = mutableListOf(),


    var totalAmount: Double = 0.0
) : BaseEntity() {
    fun updateOrderStatus(newStatus: ProductOrderStatus) {
        this.status = newStatus
    }

    fun updateTotalAmount(newTotalAmount: Double) {
        this.totalAmount = newTotalAmount
    }

    fun addOrderItem(orderItem: OrderItemEntity) {
        (items as MutableList).add(orderItem)
        orderItem.order = this
    }

    fun removeOrderItem(orderItem: OrderItemEntity) {
        (items as MutableList).remove(orderItem)
        orderItem.order = null
    }

    fun calculateTotalAmount() {
        totalAmount = items.sumOf { it.price * it.quantity }
    }

    fun confirm() {
        this.status = ProductOrderStatus.CONFIRMED
    }

    fun fail() {
        this.status = ProductOrderStatus.FAILED
    }
}

