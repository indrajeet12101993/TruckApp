package com.truckintransit.operator.fragment.vehicleadd


import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction

import com.truckintransit.operator.R
import com.truckintransit.operator.base.BaseFragment
import com.truckintransit.operator.constants.AppConstants
import kotlinx.android.synthetic.main.fragment_add_driver_basic_infro_two.view.*


class AddVehicleBasicInfroTwoFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View= inflater.inflate(R.layout.fragment_add_driver_basic_infro_two, container, false)
        view.tv_insurance.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_right_black_24dp, 0)
        view.tv_rc.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_right_black_24dp, 0)
        view.tv_permit_validity.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_right_black_24dp, 0)
        view.tv_puc.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_right_black_24dp, 0)
        view.tv_fitness.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_right_black_24dp, 0)
        view.tv_fast_tag_number.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_right_black_24dp, 0)
        view.tv_insurance.setOnClickListener {
            replaceFragment(AddVehicleInsuranceFragment())
        }
        view.tv_rc.setOnClickListener {
            replaceFragment(AddVehicleRcFragment())
        }
        view.tv_permit_validity.setOnClickListener {
            replaceFragment(AddPermitVehicleFragment())
        }
        view.tv_puc.setOnClickListener {
            replaceFragment(AddVehiclePucFragment())
        }
        view.tv_fitness.setOnClickListener {
            replaceFragment(AddFitnessVehichleFragment())
        }
        view.tv_fast_tag_number.setOnClickListener {
            replaceFragment(AddVehicleFastTagNumber())
        }

        if(AppConstants.ISDONE5 =="done"){
            view.tv_insurance.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);

        }

        if(AppConstants.ISDONE6 =="done"){
            view.tv_rc.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);

        }
        if(AppConstants.ISDONE7 =="done"){
            view.tv_permit_validity.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);

        }
        if(AppConstants.ISDONE8 =="done"){
            view.tv_puc.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);

        }
        if(AppConstants.ISDONE9 =="done"){
            view.tv_fitness.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);

        }
        if(AppConstants.ISDONE10 =="done"){
            view.tv_fast_tag_number.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);

        }




        return view
    }

    private fun replaceFragment(fragment: androidx.fragment.app.Fragment) {

        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.place_holder_for_fragment, fragment)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.addToBackStack(null)
        transaction.commit()
    }




}
