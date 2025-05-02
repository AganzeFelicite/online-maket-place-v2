package com.online_market_place.online_market_place.order.mappers

import com.online_market_place.online_market_place.order.dto.OrderCreateDTO
import com.online_market_place.online_market_place.order.dto.OrderItemCreateDTO
import com.online_market_place.online_market_place.order.dto.OrderStatusUpdateDTO
import com.online_market_place.online_market_place.order.entities.OrderEntity
import com.online_market_place.online_market_place.order.entities.OrderItemEntity

import com.online_market_place.online_market_place.product.entities.ProductEntity
import com.online_market_place.online_market_place.user.entities.UserEntity
import com.online_market_place.online_market_place.user.mappers.toUserResponse


class OrderMapper {
    fun map(order: OrderEntity): OrderCreateDTO.Output {

        return OrderCreateDTO.Output(
            id = order.id,
            user = order.user.toUserResponse(),
            status = order.status,
            createdAt = order.createdAt,
            items = order.items.map { map(it) }
        )
    }

    fun map(
        orderRequest: OrderCreateDTO.Input, user: UserEntity, productMap: Map<Long, ProductEntity>
    ): OrderEntity {
        val order = OrderEntity(
            user = user,
            items = orderRequest.items.map {
                val product = productMap[it.productId]
                    ?: throw IllegalArgumentException("Product not found with ID: ${it.productId}")
                map(it, product)
            }
        )

        order.items.forEach { it.order = order }
        return order
    }

    fun map(orderItemCreateDTO: OrderItemCreateDTO.Input, product: ProductEntity): OrderItemEntity {
        return OrderItemEntity(
            product = product,
            quantity = orderItemCreateDTO.quantity,
            price = product.price,
            order = null
        )
    }

    fun map(orderItemEntity: OrderItemEntity): OrderItemCreateDTO.Output {
        return OrderItemCreateDTO.Output(
            id = orderItemEntity.id,
            productId = orderItemEntity.product.id,
            productName = orderItemEntity.product.name,
            quantity = orderItemEntity.quantity,
            price = orderItemEntity.price,
            totalPrice = orderItemEntity.quantity * orderItemEntity.price,
            productImageUrl = orderItemEntity.product.productImageUrl
        )
    }

    fun map(orderStatusUpdateDTO: OrderStatusUpdateDTO.Input): OrderStatusUpdateDTO.Output {
        return OrderStatusUpdateDTO.Output(
            id = orderStatusUpdateDTO.id,
            status = orderStatusUpdateDTO.status
        )
    }

    fun mapToStatusOut(orderEntity: OrderEntity): OrderStatusUpdateDTO.Output {
        return OrderStatusUpdateDTO.Output(
            id = orderEntity.id,
            status = orderEntity.status
        )
    }
}

