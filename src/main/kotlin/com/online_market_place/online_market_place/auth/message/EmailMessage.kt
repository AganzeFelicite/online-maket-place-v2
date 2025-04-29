package com.online_market_place.online_market_place.auth.message

// TODO Belongs to the notification service
data class EmailMessage(
    val to: String,
    val subject: String,
    val body: String,

)
