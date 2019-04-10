package com.truckintransit.operator.pojo.bookingAccept

data class Result(
    val bookingid: String,
    val bookingnumber: String,
    val drivername: String,
    val drivernumber: String,
    val startrideotp: String
)