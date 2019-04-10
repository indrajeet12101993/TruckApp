package com.truckintransit.operator.fragment.vehicleadd


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.textfield.TextInputEditText
import com.truckintransit.operator.R
import com.truckintransit.operator.base.BaseApplication
import com.truckintransit.operator.base.BaseFragment
import com.truckintransit.operator.callbackInterface.ListnerForDialog
import com.truckintransit.operator.constants.AppConstants
import com.truckintransit.operator.dataprefence.DataManager
import com.truckintransit.operator.newtworking.ApiRequestClient
import com.truckintransit.operator.pojo.ResponseFromServerGeneric
import com.truckintransit.operator.utils.UtiliyMethods
import com.truckintransit.operator.utils.Validation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_add_vehicle_fast_tag_number.view.*


class AddVehicleFastTagNumber :  BaseFragment() , ListnerForDialog {
    private lateinit var et_fast_tag_number: TextInputEditText
    private lateinit var mTagNumber: String
    private var mCompositeDisposable_update_Photo: CompositeDisposable? = null
    private lateinit var dataManager: DataManager


    override fun selctok() {
        activity!!.supportFragmentManager.popBackStack()
        AppConstants.ISDONE10 ="done"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View= inflater.inflate(R.layout.fragment_add_vehicle_fast_tag_number, container, false)
        view.tv_fast_tag.text=getString(R.string.add_fast_tag_number)

        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()
        et_fast_tag_number=view.et_fast_tag_number


        view.save.setOnClickListener {
            if(!validationForAddDriverAdaharPhoto()){
                uploadingUserPhoto()


            }


        }
        return view
    }

    // api call for user registration
    private fun uploadingUserPhoto() {

        showDialogLoading()



        mCompositeDisposable_update_Photo = CompositeDisposable()

        mCompositeDisposable_update_Photo?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServerVehicleFastTagNumber(dataManager.getAdminId()!!,dataManager.getVehicleId()!!,mTagNumber,"true")
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



}
