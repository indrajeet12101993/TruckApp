package com.truckintransit.operator.fragment.location


import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.truckintransit.operator.R
import com.truckintransit.operator.base.BaseFragment


class DriverLoadingLocationFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_loading_location, container, false)
    }


}
