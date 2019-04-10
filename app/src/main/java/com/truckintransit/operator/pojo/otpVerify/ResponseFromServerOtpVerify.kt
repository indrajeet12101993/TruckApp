package com.truckintransit.operator.pojo.otpVerify

data class ResponseFromServerOtpVerify(
    val response_code: String,
    val response_message: String,
    val result: List<Result>
)