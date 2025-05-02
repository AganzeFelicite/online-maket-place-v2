package com.online_market_place.online_market_place.auth.exception

import com.online_market_place.online_market_place.common.exceptions.BaseException

class TokenBlacklistException(message: String, cause: String = "FAILED TO BLACKLIST TOKEN") :
    BaseException(message, cause)