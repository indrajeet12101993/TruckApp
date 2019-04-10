package com.truckintransit.operator.base

import android.app.Application
import android.content.res.Configuration

import com.truckintransit.operator.dataprefence.DataManager
import com.truckintransit.operator.dataprefence.SharedPrefsHelper
import com.truckintransit.operator.notification.NotificationHelper
import com.truckintransit.operator.utils.NotificationUtils

import android.content.Context

import android.util.Log
import com.truckintransit.operator.utils.LocaleManager


class BaseApplication : Application() {

    companion object {

        lateinit var baseApplicationInstance: BaseApplication
            private set


    }
    private val TAG = "App"
    lateinit var dataManager: DataManager
    private lateinit var notificationHelper: NotificationHelper
    lateinit var localeManager: LocaleManager



    override fun onCreate() {
        super.onCreate()

        baseApplicationInstance = this
        val sharedPrefsHelper = SharedPrefsHelper(applicationContext)
        dataManager = DataManager(sharedPrefsHelper)
        val notificationUtils = NotificationUtils(applicationContext)
        notificationHelper = NotificationHelper(notificationUtils)


    }


    fun getdatamanger(): DataManager {

        return dataManager
    }

    fun getNotificationHelper(): NotificationHelper {

        return notificationHelper
    }





    override fun attachBaseContext(base: Context) {
        localeManager = LocaleManager(base)
        super.attachBaseContext(localeManager.setLocale(base))

        Log.d(TAG, "attachBaseContext")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localeManager.setLocale(this)

    }


}