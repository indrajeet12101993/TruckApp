package com.truckintransit.operator.pojo.vehicleDetailList

data class VehicleInsurance(
    val id: String,
    val admin_id: String,
    val vehicle_id: String,
    val company_name: String,
    val insurance_date: String,
    val company_photo: String,
    val insurance_status: String,
    val insurance_approve: String
)