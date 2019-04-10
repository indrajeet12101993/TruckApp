package com.truckintransit.operator.service

import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager


import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.truckintransit.operator.activity.CustomerCallActivity
import com.truckintransit.operator.base.BaseApplication
import com.truckintransit.operator.constants.AppConstants.ACOOUNTAPPROVEDCHANNELDESCRIPTION
import com.truckintransit.operator.constants.AppConstants.ACOOUNTAPPROVEDCHANNELID
import com.truckintransit.operator.constants.AppConstants.ACOOUNTAPPROVEDCHANNELNAME
import com.truckintransit.operator.constants.AppConstants.INTENTCONSTANTBRODCASTACCOUNTAPPROVED
import com.truckintransit.operator.constants.AppConstants.INTENTCONSTANTBRODCASTBooking
import com.truckintransit.operator.dataprefence.DataManager
import com.truckintransit.operator.notification.NotificationHelper





class FirebaseMessagingServiceApp: FirebaseMessagingService() {

    lateinit var dataManager: DataManager
    lateinit var mNotificationHelper: NotificationHelper

    override fun onNewToken(newToken: String?) {
        super.onNewToken(newToken)
        Log.d("firebasetoken",newToken)
    }

    override fun onMessageReceived(remoteMessage : RemoteMessage?) {
        super.onMessageReceived(remoteMessage )

        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()
        mNotificationHelper = BaseApplication.baseApplicationInstance.getNotificationHelper()

        if (remoteMessage!!.data.isNotEmpty()) {

            val paramsbody = remoteMessage.data["alldata"]


          // val gson = Gson()
          //  val json = gson.toJson(paramsbody)
            dataManager.setUserDetails(paramsbody!!)
            val intent = Intent(applicationContext, CustomerCallActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)



          /* // val pushNotification = Intent(INTENTCONSTANTBRODCASTBooking)
            //LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)

            if(UtiliyMethods.isAppIsInBackground(applicationContext)){
//
//                mNotificationHelper.registerNotificationChannelChannel(ACOOUNTAPPROVEDCHANNELID,ACOOUNTAPPROVEDCHANNELNAME,ACOOUNTAPPROVEDCHANNELDESCRIPTION)
//                mNotificationHelper.normalNotification(1,ACOOUNTAPPROVEDCHANNELID,notification.title,notification.description)

            }
            else{


//                val pushNotification = Intent(INTENTCONSTANTBRODCASTACCOUNTAPPROVED)
//                pushNotification.putExtra("messageApproved", notification.description)
//                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)
//                mNotificationHelper.registerNotificationChannelChannel(ACOOUNTAPPROVEDCHANNELID,ACOOUNTAPPROVEDCHANNELNAME,ACOOUNTAPPROVEDCHANNELDESCRIPTION)
//                mNotificationHelper.normalNotification(1,ACOOUNTAPPROVEDCHANNELID,notification.title,notification.description)

            }


*/



        }



           // in on create
//        mRegistrationBroadcastReceiver = object : BroadcastReceiver() {
//            override fun onReceive(context: Context, intent: Intent) {
//
//                // checking for type intent filter
//                if (intent.action == AppConstants.INTENTCONSTANTBRODCASTACCOUNTAPPROVED) {
//
//                    card_isapprove.visibility = View.GONE
//                    replaceFragment(DriverLocationFragment(), "Find Rider")
//                    naviagtionshowAdminApproved()
//
//                }
//            }
//        }

           // in resume
//        // register new push message receiver
//        // by doing this, the activity will be notified each time a new message arrives
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver!!,
//            IntentFilter(AppConstants.INTENTCONSTANTBRODCASTACCOUNTAPPROVED)
//        )

           // in destroy
        // LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver!!)

    }




}