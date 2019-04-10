package com.truckintransit.operator.pojo.driverDetails

data class DriverDetail(
    val id: String,
    val name: String,
    val mobile_no: String,
    val otp: String,
    val email: String,
    val city_id: String,
    val city_name: String,
    val admin_id: String,
    val driver_approve: String,
    val is_assigned: String,
    val driver_field_status: String,
    val driver_created_date: String
)