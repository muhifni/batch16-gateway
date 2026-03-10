package com.batch16.gateway.exception

import com.batch16.gateway.domain.dto.res.BaseResponse // Pastikan import BaseResponse
import io.jsonwebtoken.ExpiredJwtException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ServerWebExchange

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizeException::class)
    fun handleUnauthorizedException(
        e: UnauthorizeException,
        exchange: ServerWebExchange
    ): ResponseEntity<BaseResponse<String>> {
        return ResponseEntity(
            BaseResponse(
                message = "Unauthorized",
                error = e.message
            ),
            HttpStatus.UNAUTHORIZED
        )
    }

    @ExceptionHandler(ExpiredJwtException::class)
    fun handleJwtExpiredException(
        e: ExpiredJwtException,
        exchange: ServerWebExchange
    ): ResponseEntity<BaseResponse<String>> {
        return ResponseEntity(
            BaseResponse(
                message = "Unauthorized",
                error = "Token expired"
            ),
            HttpStatus.UNAUTHORIZED
        )
    }
}