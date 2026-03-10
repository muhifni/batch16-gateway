package com.batch16.gateway.config

import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec
import org.springframework.cloud.gateway.route.builder.PredicateSpec
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
class GatewayConfig(
    private val authHeaderFilter: AuthHeaderFilter
) {
    @Bean
    fun routes(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes()
            .route("user-management-service") { r: PredicateSpec ->
                r.path("/gateway/user-management/**")
                    .filters { f: GatewayFilterSpec ->
                        f.rewritePath(
                            "/gateway/user-management/(?<segment>.*)",
                            "/user-management/\${segment}"
                        )
                        f.filter(authHeaderFilter.apply(AuthHeaderFilter.Config()))
                    }.uri("lb://usermanagementservice")
            }
            .route("product-management-service") { r: PredicateSpec ->
                r.path("/gateway/product-management/**")
                    .filters { f: GatewayFilterSpec ->
                        f.rewritePath(
                            "/gateway/product-management/(?<segment>.*)",
                            "/product-management/\${segment}"
                        )
                        f.filter(authHeaderFilter.apply(AuthHeaderFilter.Config()))
                    }.uri("lb://productmanagementservice")
            }
            .build()
    }

    @Bean
    fun filterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        http.csrf {
            it.disable()
        }
        return http.build()
    }
}
