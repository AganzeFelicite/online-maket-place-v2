package com.online_market_place.online_market_place.auth.exception

import com.online_market_place.online_market_place.common.exception.BaseException

class EmailConflictException(
    override val message: String = "Email is already in use",
    override val errorCode: String = "EMAIL_CONFLICT"
) : BaseException(message, errorCode)
