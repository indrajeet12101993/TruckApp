package com.truckintransit.operator.pojo.vehicleList

data class ResponseFromServerVehicleList(
    val response_code: String,
    val response_message: String,
    val vehicle_detail: List<VehicleDetail>
)