package com.truckintransit.operator.pojo

data class ResponseFromServerAddVehicle(
    val response_code: String,
    val response_message: String,
    val vehicle_id: String
)