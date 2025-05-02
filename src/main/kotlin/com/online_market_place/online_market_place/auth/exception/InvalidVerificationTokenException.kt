package com.online_market_place.online_market_place.auth.exception

import com.online_market_place.online_market_place.common.exceptions.BaseException

// TODO HArd code the error code in the base constructor call
class InvalidVerificationTokenException(
    override val message: String = "Invalid or expired verification token",
    override val errorCode: String = "INVALID_TOKEN"
) : BaseException
    (message, errorCode)