package com.online_market_place.online_market_place.notification.exceptions

import com.online_market_place.online_market_place.common.exceptions.BaseException

class EmailNotSentException(
    override val message: String,
    override val errorCode: String = "EMAIL_NOT_SENT"
) : BaseException(message, errorCode)