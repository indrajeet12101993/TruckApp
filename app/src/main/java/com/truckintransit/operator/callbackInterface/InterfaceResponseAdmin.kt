package com.truckintransit.operator.callbackInterface

import com.truckintransit.operator.pojo.adminDash.ResponseFromServerAdminDashBoard

interface InterfaceResponseAdmin {

    fun resultAdmin(response: ResponseFromServerAdminDashBoard)
}