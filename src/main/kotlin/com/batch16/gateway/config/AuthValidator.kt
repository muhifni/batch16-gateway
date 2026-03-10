package com.batch16.gateway.config

import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import java.util.function.Predicate

@Component
class AuthValidator {

    // Daftar endpoint publik yang bisa diakses TANPA token JWT
    private val openApiEndpoints = listOf(
        "/auth/login",
        "/api/user/register",
        "/eureka" // opsional, jika eureka dashboard diexpose lewat gateway
    )

    // isSecure mereturn TRUE jika request PATH TIDAK ADA di dalam openApiEndpoints
    // Artinya: Jika true -> Endpoint tersebut SECURE dan BUTUH token
    // Jika false -> Endpoint tersebut PUBLIC dan token bisa diabaikan
    val isSecure: Predicate<ServerHttpRequest> = Predicate { request ->
        openApiEndpoints.none { uri ->
            request.uri.path.contains(uri)
        }
    }
}
