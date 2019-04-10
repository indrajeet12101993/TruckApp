package com.truckintransit.operator.activity

import android.content.Context
import android.os.Bundle

import com.truckintransit.operator.R
import com.truckintransit.operator.base.BaseActivity
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.ncorti.slidetoact.SlideToActView
import com.truckintransit.operator.base.BaseApplication
import com.truckintransit.operator.dataprefence.DataManager
import kotlinx.android.synthetic.main.activity_customer_call.*
import android.os.VibrationEffect
import android.os.Build
import android.os.Vibrator
import com.truckintransit.operator.newtworking.ApiRequestClient
import com.truckintransit.operator.pojo.ResponseFromServerForBookInitil
import com.truckintransit.operator.pojo.ResponseFromServerGeneric
import com.truckintransit.operator.pojo.bookingAccept.ResponseBookingAccept
import com.truckintransit.operator.pojo.bookingnotification.ResponseBookingNotification
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class CustomerCallActivity : BaseActivity() {
    lateinit var mp: MediaPlayer
    private lateinit var  slide: SlideToActView
    lateinit var countDowntimer: CountDownTimer
    lateinit var dataManager: DataManager
    private var mCompositeDisposable: CompositeDisposable? = null
    lateinit var notification1:ResponseBookingNotification
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
         setContentView(R.layout.activity_customer_call)
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()
          val gson = Gson()
//      val type = object : TypeToken<List<ResponseBookingNotification>>() {
//
//      }.type

    //  val notification = gson.fromJson(dataManager.getUserDetails()!!, type)

        val notification = Gson().fromJson(dataManager.getUserDetails(), ResponseBookingNotification::class.java)
        notification1=notification
        tv_pickup.text= notification.data1.pickup_loc
        tv_dropoff.text= notification.data1.drop_loc
        tv_material.text= notification.data1.material_type


        mp = MediaPlayer.create(this, R.raw.uber_beep)
        mp.start()
        mp.isLooping = true



        slide= findViewById<SlideToActView>(R.id.slide)

        slide.onSlideCompleteListener = object : SlideToActView.OnSlideCompleteListener {
            override fun onSlideComplete(view: SlideToActView) {
                ribbon.visibility=View.GONE

                bookingAccept(notification.data1.customer_id,dataManager.getDriverOwnId()!!,notification.data1.serviceid,
                    notification.data1.pickup_loc,
                    notification.data1.pickup_lat,
                    notification.data1.pickup_longg,
                    notification.data1.pickup2,
                    notification.data1.pickup2_lat,
                    notification.data1.pickup2_longgg,
                    notification.data1.pickup3,
                    notification.data1.pickup3_lat,
                    notification.data1.pickup3_longg,
                    notification.data1.drop_loc,
                    notification.data1.drop_lat,
                    notification.data1.drop_longg,
                    notification.data1.dropoff2,
                    notification.data1.drop2_lat,
                    notification.data1.drop2_longg,
                    notification.data1.dropoff3,
                    notification.data1.drop3_lat,
                    notification.data1.drop3_longg,
                    notification.data1.truckid,
                    notification.data1.optionshelper,
                    notification.data1.vehiclebodytype,
                    notification.data1.vehiclefueltypeid,
                    notification.data1.vehicletypeid,
                    notification.data1.material_type,
                    notification.data1.coupon,
                    notification.data1.freightpayment,
                    notification.data1.payment_mode,
                    notification.data1.titcredits)




            }
        }

        tv_reject.setOnClickListener {


            bookingcancel(notification.data1.customer_id,"",notification.data1.serviceid,
                notification.data1.pickup_loc,
                notification.data1.pickup_lat,
                notification.data1.pickup_longg,
                notification.data1.pickup2,
                notification.data1.pickup2_lat,
                notification.data1.pickup2_longgg,
                notification.data1.pickup3,
                notification.data1.pickup3_lat,
                notification.data1.pickup3_longg,
                notification.data1.drop_loc,
                notification.data1.drop_lat,
                notification.data1.drop_longg,
                notification.data1.dropoff2,
                notification.data1.drop2_lat,
                notification.data1.drop2_longg,
                notification.data1.dropoff3,
                notification.data1.drop3_lat,
                notification.data1.drop3_longg,
                notification.data1.truckid,
                notification.data1.optionshelper,
                notification.data1.vehiclebodytype,
                notification.data1.vehiclefueltypeid,
                notification.data1.vehicletypeid,
                notification.data1.material_type,
                notification.data1.coupon,
                notification.data1.freightpayment,
                notification.data1.payment_mode,
                notification.data1.titcredits)


        }


        startCountDownTimer(60000, 1000)
        shakeItBaby(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        mp.release()
        if(countDowntimer!=null){
            countDowntimer.cancel()
        }

    }

    // start counttimer for 60 seconds
    fun startCountDownTimer(duration: Long, interval: Long) {
        countDowntimer = object : CountDownTimer(duration, interval) {
            override fun onFinish() {
                tv_count_timer.text = "0"
                bookingcancel(notification1.data1.customer_id,"",notification1.data1.serviceid,
                    notification1.data1.pickup_loc,
                    notification1.data1.pickup_lat,
                    notification1.data1.pickup_longg,
                    notification1.data1.pickup2,
                    notification1.data1.pickup2_lat,
                    notification1.data1.pickup2_longgg,
                    notification1.data1.pickup3,
                    notification1.data1.pickup3_lat,
                    notification1.data1.pickup3_longg,
                    notification1.data1.drop_loc,
                    notification1.data1.drop_lat,
                    notification1.data1.drop_longg,
                    notification1.data1.dropoff2,
                    notification1.data1.drop2_lat,
                    notification1.data1.drop2_longg,
                    notification1.data1.dropoff3,
                    notification1.data1.drop3_lat,
                    notification1.data1.drop3_longg,
                    notification1.data1.truckid,
                    notification1.data1.optionshelper,
                    notification1.data1.vehiclebodytype,
                    notification1.data1.vehiclefueltypeid,
                    notification1.data1.vehicletypeid,
                    notification1.data1.material_type,
                    notification1.data1.coupon,
                    notification1.data1.freightpayment,
                    notification1.data1.payment_mode,
                    notification1.data1.titcredits)
               // showSnackBar("No Response By Driver")

               // finish()

            }

            override fun onTick(millisUntilFinished: Long) {
                val time: Int = millisUntilFinished.toInt()
                val seconds: Int = time / 1000
              //  val minutes: Int = (time / (1000 * 60)) % 60;
                tv_count_timer.text = seconds.toString()


            }
        }.start()

    }

    // Vibrate for 150 milliseconds
    private fun shakeItBaby(context: Context) {
        if (Build.VERSION.SDK_INT >= 26) {
            (context.getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(VibrationEffect.createOneShot(60000, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            (context.getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(60000)
        }
    }


    private fun bookingcancel(
        customer_id: String,
        s1: String,
        serviceid: String,
        pickup_loc: String,
        pickup_lat: String,
        pickup_longg: String,
        pickup2: String,
        pickup2_lat: String,
        pickup2_longgg: String,
        pickup3: String,
        pickup3_lat: String,
        pickup3_longg: String,
        drop_loc: String,
        drop_lat: String,
        drop_longg: String,
        dropoff2: String,
        drop2_lat: String,
        drop2_longg: String,
        dropoff3: String,
        drop3_lat: String,
        drop3_longg: String,
        truckid: String,
        optionshelper: String,
        vehiclebodytype: String,
        vehiclefueltypeid: String,
        vehicletypeid: String,
        material_type: String,
        coupon: String,
        freightpayment: String,
        payment_mode: String,
        titcredits: String
    ) {



        showDialogLoading()

        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServerBooking(
                    customer_id,
                    s1,
                    serviceid,
                    pickup_loc,
                    pickup_lat,
                    pickup_longg,
                    pickup2,
                    pickup2_lat,
                    pickup2_longgg,
                    pickup3,
                    pickup3_lat,
                    pickup3_longg,
                    drop_loc,
                    drop_lat,
                    drop_longg,
                    dropoff2,
                    drop2_lat,
                    drop2_longg,
                    dropoff3,
                    drop3_lat,
                    drop3_longg,
                    truckid,
                    optionshelper,
                    vehiclebodytype,
                    vehiclefueltypeid,
                    vehicletypeid,
                    material_type,
                    coupon,
                    freightpayment,
                    payment_mode,
                    titcredits
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        )
    }

    // handle sucess response of api call of get transport
    private fun handleResponse(responseFromSerevr: ResponseFromServerGeneric) {
        mCompositeDisposable?.clear()
        if(countDowntimer!=null){
            countDowntimer.cancel()
        }

       // finish()



    }


    // handle failure response of api call of get transport
    private fun handleError(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }

    private fun bookingAccept(
        customer_id: String,
        s1: String,
        serviceid: String,
        pickup_loc: String,
        pickup_lat: String,
        pickup_longg: String,
        pickup2: String,
        pickup2_lat: String,
        pickup2_longgg: String,
        pickup3: String,
        pickup3_lat: String,
        pickup3_longg: String,
        drop_loc: String,
        drop_lat: String,
        drop_longg: String,
        dropoff2: String,
        drop2_lat: String,
        drop2_longg: String,
        dropoff3: String,
        drop3_lat: String,
        drop3_longg: String,
        truckid: String,
        optionshelper: String,
        vehiclebodytype: String,
        vehiclefueltypeid: String,
        vehicletypeid: String,
        material_type: String,
        coupon: String,
        freightpayment: String,
        payment_mode: String,
        titcredits: String
    ) {



        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServerBookingAccept(
                    customer_id,
                    s1,
                    serviceid,
                    pickup_loc,
                    pickup_lat,
                    pickup_longg,
                    pickup2,
                    pickup2_lat,
                    pickup2_longgg,
                    pickup3,
                    pickup3_lat,
                    pickup3_longg,
                    drop_loc,
                    drop_lat,
                    drop_longg,
                    dropoff2,
                    drop2_lat,
                    drop2_longg,
                    dropoff3,
                    drop3_lat,
                    drop3_longg,
                    truckid,
                    optionshelper,
                    vehiclebodytype,
                    vehiclefueltypeid,
                    vehicletypeid,
                    material_type,
                    coupon,
                    freightpayment,
                    payment_mode,
                    titcredits
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseAccept, this::handleErrorAccept)
        )
    }

    // handle sucess response of api call of get transport
    private fun handleResponseAccept(responseFromSerevr: ResponseBookingAccept) {
        mCompositeDisposable?.clear()
         dataManager.setBookingconfirmId(responseFromSerevr.result.bookingid)
        launchActivityWithFinish<BookingAcceptActvity>()

    }


    // handle failure response of api call of get transport
    private fun handleErrorAccept(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }
}
