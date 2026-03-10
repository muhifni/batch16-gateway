package com.batch16.gateway.config

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.crypto.SecretKey

@Component
class JwtUtil(
    // Mengambil secret key dari application.yml (app.jwt.secret-key atau jwt.secret-key)
    @Value("\${jwt.secret-key}")
    private val secretKeyStr: String
) {

    private val key: SecretKey by lazy {
        Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyStr))
    }

    /**
     * Decode dan validasi token JWT.
     * Mengembalikan Claims (berfungsi sebagai Map) jika valid.
     * Akan melempar JwtException jika token invalid/expired.
     */
    fun decode(token: String): Claims {
        return try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: JwtException) {
            throw RuntimeException("INVALID OR EXPIRED TOKEN")
        } catch (e: IllegalArgumentException) {
            throw RuntimeException("TOKEN IS EMPTY OR NULL")
        }
    }
}
