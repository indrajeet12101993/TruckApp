package com.truckintransit.operator.pojo.driverDash

data class BasicInfo(
    val id: String,
    val name: String,
    val mobile_no: String,
    val otp: String,
    val email: String,
    val city_name: String,
    val admin_id: String,
    val is_assigned: String,
    val driver_field_status: String,
    val driver_pic: String,
    val driver_approve: String
)