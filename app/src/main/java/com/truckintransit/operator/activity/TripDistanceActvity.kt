package com.truckintransit.operator.activity


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.truckintransit.operator.R
import com.truckintransit.operator.base.BaseActivity
import com.truckintransit.operator.base.BaseApplication
import com.truckintransit.operator.dataprefence.DataManager
import com.truckintransit.operator.newtworking.ApiRequestClient
import com.truckintransit.operator.pojo.ResponseFromServerGeneric
import com.truckintransit.operator.pojo.bookingnotification.ResponseBookingNotification
import com.truckintransit.operator.pojo.endtrip.ResponseForEndTrip
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_trip_distance_actvity.*

class TripDistanceActvity : BaseActivity(){
    private var mCompositeDisposable_update_Photo: CompositeDisposable? = null
    lateinit var dataManager: DataManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_distance_actvity)
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()
        val notification = Gson().fromJson(dataManager.getUserDetails(), ResponseBookingNotification::class.java)

        tv_pickup.text = notification.data1.pickup_loc
        tv_dropoff.text = notification.data1.drop_loc

        tvend.setOnClickListener {

            endRide(dataManager.getBookingConfirmId())
        }
        tvnavigate.setOnClickListener {
            val gmmIntentUri = Uri.parse("google.navigation:q=" + notification.data1.drop_loc)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }



    }

    private fun endRide( bookingConfirmId: String?) {

        showDialogLoading()


        mCompositeDisposable_update_Photo = CompositeDisposable()

        mCompositeDisposable_update_Photo?.add(
            ApiRequestClient.createREtrofitInstance()
                .endRidee(bookingConfirmId!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseUser_Photo, this::handleError_user_photo)
        )
    }


    // handle sucess response of api call registration
    private fun handleResponseUser_Photo(response: ResponseForEndTrip) {
        hideDialogLoading()
        mCompositeDisposable_update_Photo?.clear()
        if(response.response_code=="1"){
            showDialogWithDismiss(response.response_message+" you cant end ride")
        }else{
            tvtripdistance.text = "Trip Distance(Kms): " + response.Result.trip_distance
            tvduration.text = "Trip Duration(in minutes) : " +  response.Result.trip_time_minutes
            tvprice.text = "Total price (Rs ) : " + response.Result.net_amount
            tvend.visibility=View.GONE
            showDialogWithDismiss(response.response_message+" your ride is ended Collect money")

        }




    }


    // handle failure response of api call registration
    private fun handleError_user_photo(error: Throwable) {

        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable_update_Photo?.clear()

    }
}
