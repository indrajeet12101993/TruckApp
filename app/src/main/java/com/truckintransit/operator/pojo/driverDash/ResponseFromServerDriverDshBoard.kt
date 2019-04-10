package com.truckintransit.operator.pojo.driverDash

data class ResponseFromServerDriverDshBoard(
    val response_code: String,
    val response_message: String,
    val Basic_info: List<BasicInfo>
)