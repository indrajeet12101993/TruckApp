package com.truckintransit.operator.pojo.driverlist

data class DriverDetail(
    val id: String,
    val name: String,
    val photo: String,
    val active: String,
    val vehicleid: Any,
    val vehiclename: Any,
    val is_assigned: String
)