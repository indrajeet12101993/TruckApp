package com.truckintransit.operator.pojo.driverDetails

data class DlPhoto(
    val id: String,
    val driver_id: String,
    val dl_expiry_date: String,
    val dl_number: String,
    val dl_photo: String,
    val dl_backphoto: String,
    val dl_expiry_status: String,
    val dl_approve: String,
    val dl_status: String
)