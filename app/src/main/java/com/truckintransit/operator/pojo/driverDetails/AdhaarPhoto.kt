package com.truckintransit.operator.pojo.driverDetails

data class AdhaarPhoto(
    val id: String,
    val driver_id: String,
    val adhar_no: String,
    val adhar_pic: String,
    val adhar_backpic: String,
    val adhar_approve: String,
    val adhar_status: String
)