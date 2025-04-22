import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.online_market_place.online_market_place.dto.order.OrderResponse
import com.online_market_place.online_market_place.dto.review.ReviewResponse
import com.online_market_place.online_market_place.entiy.enum_.UserRole
import java.time.LocalDateTime

interface BaseUserResponse

data class UserResponse(
    val id: Long,
    val email: String,
    val username: String,
    val role: Set<UserRole>
) : BaseUserResponse

data class UserDetailedResponse(
    val id: Long,
    val email: String,
    val username: String,
    val role: Set<UserRole>,
    val orders: List<OrderResponse>,
    val reviews: List<ReviewResponse>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) : BaseUserResponse
