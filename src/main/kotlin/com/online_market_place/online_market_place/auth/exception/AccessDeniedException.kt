package com.online_market_place.online_market_place.auth.exception

import com.online_market_place.online_market_place.common.exceptions.BaseException

class AccessDeniedException :
    BaseException("Access is denied", "FORBIDDEN")
