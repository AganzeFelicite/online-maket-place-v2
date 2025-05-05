package com.online_market_place.online_market_place.auth.exception

import com.online_market_place.online_market_place.common.exceptions.BaseException

class TokenBlacklistException(message: String) : BaseException(
    message,
    "FAILED_TO_BLACKLIST_TOKEN"
)
