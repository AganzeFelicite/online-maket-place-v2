package com.online_market_place.online_market_place.common.exceptions

class  ResourceNotFoundException(
    override val message: String,
    override val errorCode: String = "RESOURCE_NOT_FOUND"
) : BaseException(message, errorCode)