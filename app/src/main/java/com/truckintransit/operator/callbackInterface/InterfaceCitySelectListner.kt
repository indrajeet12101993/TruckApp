package com.truckintransit.operator.callbackInterface

import com.truckintransit.operator.pojo.citylist.Result


interface InterfaceCitySelectListner {
    fun onItemClickState(result: Result)
}