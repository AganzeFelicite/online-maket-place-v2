package com.online_market_place.online_market_place.auth.exception

import com.online_market_place.online_market_place.common.exception.BaseException

class InvalidVerificationTokenException(
    override val message: String = "Invalid or expired verification token",
    override val errorCode: String = "INVALID_TOKEN"
) : BaseException
    (message, errorCode)