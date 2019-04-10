package com.truckintransit.operator.activity


import android.os.Bundle
import com.google.gson.Gson
import com.truckintransit.operator.R
import com.truckintransit.operator.base.BaseActivity
import com.truckintransit.operator.base.BaseApplication
import com.truckintransit.operator.dataprefence.DataManager
import com.truckintransit.operator.pojo.bookingnotification.ResponseBookingNotification
import kotlinx.android.synthetic.main.activity_booking_accept_actvity.*
import android.content.Intent

import android.net.Uri
import com.truckintransit.operator.constants.AppConstants
import com.truckintransit.operator.newtworking.ApiRequestClient
import com.truckintransit.operator.pojo.ResponseFromServerGeneric
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class BookingAcceptActvity : BaseActivity() {

    private var mCompositeDisposable_update_Photo: CompositeDisposable? = null
    lateinit var dataManager: DataManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_accept_actvity)
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()
        val notification = Gson().fromJson(dataManager.getUserDetails(), ResponseBookingNotification::class.java)

        tv_pickup.text = notification.data1.pickup_loc
        tv_dropoff.text = notification.data1.drop_loc
        tv_customername.text = "Customer Name : " + notification.data1.customer_id
        tv_booking.text = "Date Of booking  : " + notification.data1.created

        tvcallsender.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:" + "8368771983")
            startActivity(callIntent)
        }

        tvnavigate.setOnClickListener {
            val gmmIntentUri = Uri.parse("google.navigation:q=" + notification.data1.pickup_loc)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        tvgo.setOnClickListener {
            if(et_otp.text.toString()==""){
                et_otp.setError(getString(R.string.valid_otp))
            }else{
                startRide(et_otp.text.toString(),dataManager.getBookingConfirmId())
            }


        }

        tvcancel.setOnClickListener {
            finish()
        }

    }
    private fun startRide(otp: String, bookingConfirmId: String?) {

        showDialogLoading()


        mCompositeDisposable_update_Photo = CompositeDisposable()

        mCompositeDisposable_update_Photo?.add(
            ApiRequestClient.createREtrofitInstance()
                .startRidee(bookingConfirmId!!,otp)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseUser_Photo, this::handleError_user_photo)
        )
    }


    // handle sucess response of api call registration
    private fun handleResponseUser_Photo(response: ResponseFromServerGeneric) {
        hideDialogLoading()

        if(response.response_code=="1"){
            showDialogWithDismiss(response.response_message+" you cant start ride")
        }else{
            showDialogWithDismiss(response.response_message+" your ride is start")
            launchActivityWithFinish<TripDistanceActvity>()
        }


        mCompositeDisposable_update_Photo?.clear()

    }


    // handle failure response of api call registration
    private fun handleError_user_photo(error: Throwable) {

        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable_update_Photo?.clear()

    }
}




