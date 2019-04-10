package com.truckintransit.operator.pojo.bookingnotification

data class ResponseBookingNotification(
    val body: String,
    val data1: Data,
    val driver_id: String,
    val title: String
)