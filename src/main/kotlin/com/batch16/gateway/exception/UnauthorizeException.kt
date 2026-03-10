package com.batch16.gateway.exception

// Exception ini mewarisi RuntimeException dan menerima parameter message
class UnauthorizeException(message: String) : RuntimeException(message)
