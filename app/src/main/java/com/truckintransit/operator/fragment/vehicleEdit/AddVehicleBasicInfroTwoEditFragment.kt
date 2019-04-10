package com.truckintransit.operator.fragment.vehicleEdit


import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction

import com.truckintransit.operator.R
import com.truckintransit.operator.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_add_vehicle_basic_infro_two_edit.view.*


class AddVehicleBasicInfroTwoEditFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View=  inflater.inflate(R.layout.fragment_add_vehicle_basic_infro_two_edit, container, false)

        view.tv_insurance.setOnClickListener {
            replaceFragment(AddVehicleInsuranceEditFragment())
        }
        view.tv_rc.setOnClickListener {
            replaceFragment(AddVehicleRcEditFragment())
        }
        view.tv_permit_validity.setOnClickListener {
            replaceFragment(AddPermitVehicleEditFragment())
        }
        view.tv_puc.setOnClickListener {
            replaceFragment(AddVehiclePucEditFragment())
        }
        view.tv_fitness.setOnClickListener {
            replaceFragment(AddFitnessVehichleEditFragment())
        }
        view.tv_fast_tag_number.setOnClickListener {
            replaceFragment(AddVehicleFastTagEditNumber())
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
