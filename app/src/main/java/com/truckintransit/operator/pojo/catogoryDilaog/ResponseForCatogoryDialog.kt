package com.truckintransit.operator.pojo.catogoryDilaog

data class ResponseForCatogoryDialog(
    val response_code: String,
    val response_message: String,
    val Result: List<Result>
)