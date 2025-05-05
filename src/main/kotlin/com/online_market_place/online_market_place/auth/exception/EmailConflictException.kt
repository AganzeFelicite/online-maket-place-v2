package com.online_market_place.online_market_place.auth.exception

import com.online_market_place.online_market_place.common.exceptions.BaseException

class EmailConflictException(
    override val message: String = "",
    override val errorCode: String = "EMAIL_CONFLICT"
) : BaseException(message, errorCode)
