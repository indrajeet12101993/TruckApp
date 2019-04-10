package com.truckintransit.operator.dataprefence

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.truckintransit.operator.constants.AppConstants
import com.truckintransit.operator.constants.AppConstants.PREFILENAME
import com.truckintransit.operator.constants.AppConstants.PREF_KEY_ADMIN_ID
import com.truckintransit.operator.constants.AppConstants.PREF_KEY_DRIVER_ID
import com.truckintransit.operator.constants.AppConstants.PREF_KEY_DRIVER_OWN_ID
import com.truckintransit.operator.constants.AppConstants.PREF_KEY_DRIVER_TYPE
import com.truckintransit.operator.constants.AppConstants.PREF_KEY_IS_BLOCKED
import com.truckintransit.operator.constants.AppConstants.PREF_KEY_IS_LOGGED_IN
import com.truckintransit.operator.constants.AppConstants.PREF_KEY_LANGUAGE
import com.truckintransit.operator.constants.AppConstants.PREF_KEY_USER_DETAILS
import com.truckintransit.operator.constants.AppConstants.PREF_KEY_VEHICLE_ID
import com.truckintransit.operator.constants.AppConstants.PREF_KEY_bookingid


class SharedPrefsHelper(context: Context) {
    internal var mSharedPreferences: SharedPreferences

    init {
        mSharedPreferences = context.getSharedPreferences(PREFILENAME, MODE_PRIVATE)
    }


    var loggedInMode: Boolean
        get() = mSharedPreferences.getBoolean(PREF_KEY_IS_LOGGED_IN, false)
        set(loggedInMode) = with(mSharedPreferences.edit()) {
            putBoolean(PREF_KEY_IS_LOGGED_IN, loggedInMode)
            apply()
        }

    var userDetails: String?
        get() = mSharedPreferences.getString(PREF_KEY_USER_DETAILS,null)
        set(userDetails) = with(mSharedPreferences.edit()) {
            putString(PREF_KEY_USER_DETAILS, userDetails)
            apply()
        }
    var driverId: String?
        get() = mSharedPreferences.getString(PREF_KEY_DRIVER_ID,null)
        set(driverId) = with(mSharedPreferences.edit()) {
            putString(PREF_KEY_DRIVER_ID, driverId)
            apply()
        }

    var driverOwnId: String?
        get() = mSharedPreferences.getString(PREF_KEY_DRIVER_OWN_ID,null)
        set(driverOwnId) = with(mSharedPreferences.edit()) {
            putString(PREF_KEY_DRIVER_OWN_ID, driverOwnId)
            apply()
        }

    var driverType: String?
        get() = mSharedPreferences.getString(PREF_KEY_DRIVER_TYPE,null)
        set(driverType) = with(mSharedPreferences.edit()) {
            putString(PREF_KEY_DRIVER_TYPE, driverType)
            apply()
        }

    var isBlocked: String?
        get() = mSharedPreferences.getString(PREF_KEY_IS_BLOCKED,null)
        set(isBlocked) = with(mSharedPreferences.edit()) {
            putString(PREF_KEY_IS_BLOCKED, isBlocked)
            apply()
        }

    var adminId: String?
        get() = mSharedPreferences.getString(PREF_KEY_ADMIN_ID,null)
        set(adminId) = with(mSharedPreferences.edit()) {
            putString(PREF_KEY_ADMIN_ID, adminId)
            apply()
        }

    var vehicleId: String?
        get() = mSharedPreferences.getString(PREF_KEY_VEHICLE_ID,null)
        set(vehicleId) = with(mSharedPreferences.edit()) {
            putString(PREF_KEY_VEHICLE_ID, vehicleId)
            apply()
        }

    var language: String?
        get() = mSharedPreferences.getString(PREF_KEY_LANGUAGE,null)
        set(language) = with(mSharedPreferences.edit()) {
            putString(PREF_KEY_LANGUAGE, language)
            apply()
        }
    var bookingid: String?
        get() = mSharedPreferences.getString(PREF_KEY_bookingid,null)
        set(bookingid) = with(mSharedPreferences.edit()) {
            putString(PREF_KEY_bookingid, bookingid)
            apply()
        }

    fun clear() {
        with(mSharedPreferences.edit()) {
            clear()
            apply()
        }


    }

}