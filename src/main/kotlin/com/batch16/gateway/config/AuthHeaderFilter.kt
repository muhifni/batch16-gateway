package com.batch16.gateway.config

import com.batch16.gateway.exception.UnauthorizeException
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component

@Component
class AuthHeaderFilter(
    private val authValidator: AuthValidator,
    private val jwtUtil: JwtUtil
) : AbstractGatewayFilterFactory<AuthHeaderFilter.Config>(Config::class.java) {

    class Config // Class kosong wajib untuk factory

    fun isAuthExist(request: ServerHttpRequest): Boolean {
        return request.headers.containsKey("Authorization")
    }

    fun getToken(request: ServerHttpRequest): String? {
        val authorizationHeader = request.headers.getOrEmpty("Authorization")
        if (authorizationHeader.isEmpty()) {
            return null
        }

        val bearer = authorizationHeader[0]
        return if (bearer.split(" ").size == 2) {
            bearer.split(" ")[1]
        } else {
            null
        }
    }

    override fun apply(config: Config): GatewayFilter {
        return GatewayFilter { exchange, chain ->
            val request = exchange.request

            if (authValidator.isSecure.test(request)) {
                if (!isAuthExist(request)) {
                    throw UnauthorizeException("UNAUTHORIZED IN AUTHENTICATION")
                }

                val token = getToken(request) ?: throw UnauthorizeException("UNAUTHORIZED (TOKEN NOT FOUND)")

                val claims = jwtUtil.decode(token)
                exchange.request.mutate()
                    .header("X-USER-ID", claims["userId"].toString())
                    .header("Authority", claims["Authority"].toString())
                    .build()
            }

            chain.filter(exchange)
        }
    }
}
