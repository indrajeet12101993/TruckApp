package com.truckintransit.operator.service

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationResult
import com.truckintransit.operator.base.BaseApplication
import com.truckintransit.operator.constants.AppConstants.ACTION_PROCESS_UPDATES
import com.truckintransit.operator.notification.NotificationHelper


class LocationUpdatesIntentService : IntentService("LocationUpdatesIntentService") {


    lateinit var mNotificationHelper: NotificationHelper
    var result: LocationResult?=null

    override fun onHandleIntent(intent: Intent?) {
        mNotificationHelper = BaseApplication.baseApplicationInstance.getNotificationHelper()
        if (intent != null) {
            val action = intent.getAction();

            if (ACTION_PROCESS_UPDATES.equals(action)) {
                 result = LocationResult.extractResult(intent);
                if (result != null) {
                    val locations: MutableList<Location> = result!!.getLocations()

                    mNotificationHelper.registerNotificationChannelChannel("1", "1", "dd")
                    mNotificationHelper.normalNotification(
                        1,
                        "1",
                        locations[0].latitude.toString(),
                        locations[0].longitude.toString()
                    )

                    /*  val locationResultHelper:LocationResultHelper  = LocationResultHelper(this,locations);
                    // Save the location data to SharedPreferences.
                    locationResultHelper.saveResults();
                    // Show notification with the location data.
                    locationResultHelper.showNotification();
                    Log.i(TAG, LocationResultHelper.getSavedLocationResult(this));*/
                }
            }

        }


    }
}
