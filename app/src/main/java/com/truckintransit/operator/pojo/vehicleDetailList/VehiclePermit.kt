package com.truckintransit.operator.pojo.vehicleDetailList

data class VehiclePermit(
    val id: String,
    val admin_id: String,
    val vehicle_id: String,
    val permit_no: String,
    val permit_valid: String,
    val permit_photo: String,
    val permit_status: String,
    val permit_approve: String
)