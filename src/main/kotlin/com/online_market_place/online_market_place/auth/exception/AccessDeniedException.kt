package com.online_market_place.online_market_place.auth.exception

import com.online_market_place.online_market_place.common.exception.BaseException


class AccessDeniedException(
    override val message: String,
    override val errorCode: String = "FORBIDDEN"
) : BaseException(message, errorCode)