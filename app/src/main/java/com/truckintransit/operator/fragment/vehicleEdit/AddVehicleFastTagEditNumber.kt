package com.truckintransit.operator.fragment.vehicleEdit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.truckintransit.operator.R
import com.truckintransit.operator.base.BaseApplication
import com.truckintransit.operator.base.BaseFragment
import com.truckintransit.operator.callbackInterface.ListnerForDialog
import com.truckintransit.operator.constants.AppConstants
import com.truckintransit.operator.dataprefence.DataManager
import com.truckintransit.operator.newtworking.ApiRequestClient
import com.truckintransit.operator.pojo.ResponseFromServerGeneric
import com.truckintransit.operator.pojo.vehicleDetailList.ResponseFromServerVehicleDetailList
import com.truckintransit.operator.utils.UtiliyMethods
import com.truckintransit.operator.utils.Validation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_add_vehicle_fast_tag_number.view.*

class AddVehicleFastTagEditNumber :  BaseFragment() , ListnerForDialog {
    private lateinit var et_fast_tag_number: TextInputEditText
    private lateinit var mTagNumber: String
    private var mCompositeDisposable_update_Photo: CompositeDisposable? = null
    private lateinit var dataManager: DataManager


    override fun selctok() {
        activity!!.supportFragmentManager.popBackStack()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_add_vehicle_fast_tag_number, container, false)
        view.tv_fast_tag.text=getString(R.string.edit_fast_tag_number)

        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()
        et_fast_tag_number=view.et_fast_tag_number


        view.save.setOnClickListener {
            if(!validationForAddDriverAdaharPhoto()){
                uploadingUserPhoto()


            }


        }
        startLoadingForDriverDetails()
        return view
    }

    // api call for user registration
    private fun uploadingUserPhoto() {

        showDialogLoading()



        mCompositeDisposable_update_Photo = CompositeDisposable()

        mCompositeDisposable_update_Photo?.add(
            ApiRequestClient.createREtrofitInstance()
                .editServerVehicleFastTagNumber(AppConstants.VEHICLEDETAILID,mTagNumber,"true")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseUser_Photo, this::handleError_user_photo)
        )
    }


    // handle sucess response of api call registration
    private fun handleResponseUser_Photo(response: ResponseFromServerGeneric) {
        hideDialogLoading()

        if (response.response_code.equals("0")) {
            UtiliyMethods.showDialogwithMessage(activity!!,response.response_message,this )



        }

        mCompositeDisposable_update_Photo?.clear()

    }


    // handle failure response of api call registration
    private fun handleError_user_photo(error: Throwable) {

        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable_update_Photo?.clear()

    }

    private fun validationForAddDriverAdaharPhoto():Boolean {


        mTagNumber = et_fast_tag_number.text.toString()



        if(Validation.isEmptyField(mTagNumber)){
            et_fast_tag_number.error = getString(R.string.error_no_text)
            et_fast_tag_number.requestFocus()
            return true

        }




        return false


    }
    //geeting  number for get individual driver details
    private fun startLoadingForDriverDetails() {


        showDialogLoading()

        mCompositeDisposable_update_Photo = CompositeDisposable()

        mCompositeDisposable_update_Photo?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServerVehicleBasiCDetails(AppConstants.VEHICLEDETAILID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        )
    }

    // handle sucess response of api call of get phone number
    private fun handleResponse(response: ResponseFromServerVehicleDetailList) {
        hideDialogLoading()
        mCompositeDisposable_update_Photo?.clear()


        if(response.vehicle_fasttap.size>0) {



            et_fast_tag_number.setText(response.vehicle_fasttap[0].vehicle_fast_tapno)

        }



    }


    // handle failure response of api call of get phone number
    private fun handleError(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable_update_Photo?.clear()


    }
}