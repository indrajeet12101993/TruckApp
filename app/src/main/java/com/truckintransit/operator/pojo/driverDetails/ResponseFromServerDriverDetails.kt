package com.truckintransit.operator.pojo.driverDetails

data class ResponseFromServerDriverDetails(
    val response_code: String,
    val response_message: String,
    val driver_detail: List<DriverDetail>,
    val driver_photo: List<DriverPhoto>,
    val dl_photo: List<DlPhoto>,
    val adhaar_photo: List<AdhaarPhoto>,
    val current_address_photo: List<CurrentAddressPhoto>
)