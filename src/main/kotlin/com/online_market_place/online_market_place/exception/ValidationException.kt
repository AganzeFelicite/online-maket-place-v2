package com.online_market_place.online_market_place.exception

import com.online_market_place.online_market_place.common.BaseException

/**
 * Validation exception
 */
class ValidationException(
    override val message: String,
    override val errorCode: String = "VALIDATION_ERROR"
) : BaseException(message, errorCode)