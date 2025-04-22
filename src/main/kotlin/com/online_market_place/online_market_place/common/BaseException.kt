package com.online_market_place.online_market_place.common

/**
 * Base exception class with error code
 */
abstract class BaseException(
    override val message: String,
    open val errorCode: String
) : RuntimeException(message)