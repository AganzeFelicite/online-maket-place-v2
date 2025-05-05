package com.online_market_place.online_market_place.auth.exception

import com.online_market_place.online_market_place.common.exceptions.BaseException
class InvalidVerificationTokenException :
    BaseException("Invalid or expired verification token", "INVALID_TOKEN")
