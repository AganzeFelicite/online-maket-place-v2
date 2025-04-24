import com.online_market_place.online_market_place.order.dto.OrderResponse
import com.online_market_place.online_market_place.review.dto.ReviewResponse
import com.online_market_place.online_market_place.user.enum_.UserRole
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
