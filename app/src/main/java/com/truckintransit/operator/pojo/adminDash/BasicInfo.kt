package com.truckintransit.operator.pojo.adminDash

data class BasicInfo(
    val id: String,
    val name: String,
    val mobile: String,
    val email: String,
    val image: String,
    val reg_type: String,
    val is_blocked: String
)