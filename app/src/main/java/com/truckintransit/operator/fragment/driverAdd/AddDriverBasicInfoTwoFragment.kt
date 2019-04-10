package com.truckintransit.operator.fragment.driverAdd


import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.truckintransit.operator.R
import com.truckintransit.operator.base.BaseApplication
import com.truckintransit.operator.base.BaseFragment
import com.truckintransit.operator.constants.AppConstants.ISDONE1
import com.truckintransit.operator.constants.AppConstants.ISDONE2
import com.truckintransit.operator.constants.AppConstants.ISDONE3
import com.truckintransit.operator.constants.AppConstants.ISDONE4
import com.truckintransit.operator.dataprefence.DataManager
import com.truckintransit.operator.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_add_driver_basic_info_two.view.*


class AddDriverBasicInfoTwoFragment : BaseFragment() {

    private lateinit var mSharedViewModel: SharedViewModel
    private lateinit var mIsMenuShow: String
    private lateinit var driver_photo: TextView
    private lateinit var driver_adahar_photo: TextView
    private lateinit var driver_dl_photo: TextView
    private lateinit var driver_current_adress: TextView


    private lateinit var dataManager: DataManager
    private lateinit var mUserDetails: List<com.truckintransit.operator.pojo.otpVerify.Result>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSharedViewModel = activity?.run {
            ViewModelProviders.of(this).get(SharedViewModel::class.java)
        } ?: throw Exception("Invalid Activity") as Throwable

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View= inflater.inflate(R.layout.fragment_add_driver_basic_info_two, container, false)

        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()
        mSharedViewModel.isMenuEdit.observe(this, Observer<String> { item ->
            transfer(item)

        }
        )


        driver_photo= view.driver_photo
        driver_adahar_photo= view.driver_adahar_photo
        driver_dl_photo= view.driver_dl_photo
        driver_current_adress= view.driver_current_adress
        driver_photo.text = getString(R.string.attach_driver_photo)
        driver_photo.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_right_black_24dp, 0);
        driver_adahar_photo.text = getString(R.string.attach_aaddhar_photo)
        driver_adahar_photo.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_right_black_24dp, 0);
        driver_dl_photo.text = getString(R.string.attach_dl_photo)
        driver_dl_photo.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_right_black_24dp, 0);
        driver_current_adress.text = getString(R.string.attach_current_address_proof_with_photo)
        driver_current_adress.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_right_black_24dp, 0);

        if(ISDONE1=="done"){
            driver_photo.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);

        }
        if(ISDONE2=="done"){
            driver_adahar_photo.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);

        }
        if(ISDONE3=="done"){
            driver_dl_photo.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);

        }
        if(ISDONE4=="done"){
            driver_current_adress.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_check_black_24dp, 0);

        }


        view.driver_photo.setOnClickListener {
            replaceFragment(AddDriverPhotoFragment())

        }
        view.driver_adahar_photo.setOnClickListener {
            replaceFragment(AddDriverAdharPhotoFragment())
        }
        view.driver_dl_photo.setOnClickListener {

            replaceFragment(AddDriverDlPhotoFragment())
        }
        view.driver_current_adress.setOnClickListener {

            replaceFragment(AddDriverAdressFragment())
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
    private fun transfer(item: String?) {
        mIsMenuShow=item!!

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {


//        if (mIsMenuShow == "True") {
//            inflater.inflate(R.menu.adddriver, menu)
//            driver_photo.text = getString(R.string.edit_driver_photo)
//            driver_adahar_photo.text = getString(R.string.edit_adhar_photo)
//            driver_dl_photo.text = getString(R.string.edit_dl_photo)
//            driver_current_adress.text = getString(R.string.edit_current_adress_photo)
//
//        } else {
//            driver_photo.text = getString(R.string.attach_driver_photo)
//            driver_adahar_photo.text = getString(R.string.attach_aaddhar_photo)
//            driver_dl_photo.text = getString(R.string.attach_dl_photo)
//            driver_current_adress.text = getString(R.string.attach_current_address_proof_with_photo)
//        }

    }


}
