package com.truckintransit.operator.pojo.vehicleDetailList

data class ResponseFromServerVehicleDetailList(
    val response_code: String,
    val response_message: String,
    val vehicle_detail: List<VehicleDetail>,
    val vehicle_rc: List<VehicleRc>,
    val vehicle_insurance: List<VehicleInsurance>,
    val vehicle_permit: List<VehiclePermit>,
    val vehicle_puc: List<VehiclePuc>,
    val vehicle_fitness: List<VehicleFitnes>,
    val vehicle_fasttap: List<VehicleFasttap>
)