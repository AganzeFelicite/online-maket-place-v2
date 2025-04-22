package com.online_market_place.online_market_place.exception

import com.online_market_place.online_market_place.common.BaseException

class InvalidVerificationTokenException(
    override val message: String = "Invalid or expired verification token",
    override val errorCode: String = "INVALID_TOKEN"
) :BaseException
    (message, errorCode)