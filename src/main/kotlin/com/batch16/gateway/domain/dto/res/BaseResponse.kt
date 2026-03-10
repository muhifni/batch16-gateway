package com.batch16.gateway.domain.dto.res

data class BaseResponse<T>(
    val message: String,
    val error: String? = null
)
