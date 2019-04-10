package com.truckintransit.operator.pojo

data class ResponseFromServerForDriverBasicDetails(
    val response_code: String,
    val response_message: String,
    val driver_id: String
)