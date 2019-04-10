package com.truckintransit.operator.pojo.notification

data class Body(
    val pickup_longg: String,
    val customer_number: String,
    val booking_date: String,
    val booking_time: String,
    val vehicle_weight: String,
    val drop_loc: String,
    val trip_distance: String,
    val material_type: String,
    val drop_longg: String,
    val drop_lat: String,
    val trip_start: String,
    val type_booking: String,
    val id: String,
    val pickup_lat: String,
    val customer_id: String,
    val custome_name: String,
    val trip_duration: String,
    val pickup_loc: String
)