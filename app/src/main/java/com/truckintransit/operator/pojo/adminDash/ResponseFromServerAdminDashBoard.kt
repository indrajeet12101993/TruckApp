package com.truckintransit.operator.pojo.adminDash

data class ResponseFromServerAdminDashBoard(
    val response_code: String,
    val response_message: String,
    val Basic_info: List<BasicInfo>,
    val Transaction_detail: List<TransactionDetail>,
    val bank_detail: List<BankDetail>
)