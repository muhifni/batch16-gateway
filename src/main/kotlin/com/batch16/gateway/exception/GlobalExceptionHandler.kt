package com.batch16.gateway.exception

import com.batch16.gateway.domain.dto.res.BaseResponse // Pastikan import BaseResponse
import io.jsonwebtoken.ExpiredJwtException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizeException::class)
    fun handleUnauthorizedException(e: UnauthorizeException): ResponseEntity<BaseResponse<String>> {

        val responseBody = BaseResponse<String>(
            message = "You are not authorized",
            error = e.message
        )

        return ResponseEntity(responseBody, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(ExpiredJwtException::class)
    fun handleJwtExpiredException(
        e: ExpiredJwtException
    ): ResponseEntity<BaseResponse<String>> {
        return ResponseEntity(
            BaseResponse(
                message = "UNAUTHORIZED",
                error = "Token expired"
            ),
            HttpStatus.UNAUTHORIZED
        )
    }
}
