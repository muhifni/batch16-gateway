import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.jwt")
data class JwtProperties(
    var secretKey: String = "",   // Base64
    var expiration: Long = 0       // millis
)
