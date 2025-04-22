package com.online_market_place.online_market_place.exception

import com.online_market_place.online_market_place.common.BaseException


class AccessDeniedException(
    override val message: String,
    override val errorCode: String = "FORBIDDEN"
) : BaseException(message, errorCode) {
    constructor() : this("Access denied")
}