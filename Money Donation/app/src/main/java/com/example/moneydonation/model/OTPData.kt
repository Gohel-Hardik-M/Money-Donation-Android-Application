import java.util.*

data class OTPData(
    val id: String = "",
    val email: String = "",
    val otp: String = "",
    val createdAt: Date = Date(),
    val expiresAt: Date = Date(),
    val isUsed: Boolean = false
)