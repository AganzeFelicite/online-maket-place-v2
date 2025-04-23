package com.online_market_place.online_market_place.exception

import com.online_market_place.online_market_place.common.BaseException

class EmailNotSentException (
    override val message: String,
    override val errorCode: String = "EMAIL_NOT_SENT"
) : BaseException(message, errorCode)