package com.online_market_place.online_market_place.order.exception

import com.online_market_place.online_market_place.common.exception.BaseException

class InsufficientInventoryException(
    override val message: String,
    override val errorCode: String = "RESOURCE_INSUFFICIENT_INVENTORY"
) : BaseException(message, errorCode)