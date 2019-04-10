package com.truckintransit.operator.pojo.driverlist

data class ResponseFromServerDriverList(
    val response_code: String,
    val response_message: String,
    val driver_detail: List<DriverDetail>
)