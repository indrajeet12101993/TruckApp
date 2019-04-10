package com.truckintransit.operator.pojo.citylist

data class ResponseFromServerCityList(
    val response_code: String,
    val response_message: String,
    val result: List<Result>
)