package com.truckintransit.operator.dataprefence

class DataManager(internal var mSharedPrefsHelper: SharedPrefsHelper) {

    fun clear() {
        mSharedPrefsHelper.clear()
    }



    fun setLoggedIn(isLogin:Boolean) {
        mSharedPrefsHelper.loggedInMode =isLogin
    }

    fun getLoggedIn():Boolean {
        return mSharedPrefsHelper.loggedInMode
    }

    fun setUserDetails(userDetails: String){
        mSharedPrefsHelper.userDetails =userDetails
    }
    fun getUserDetails():String?{
        return mSharedPrefsHelper.userDetails
    }

    fun setDriverId(driverId: String){
        mSharedPrefsHelper.driverId =driverId
    }
    fun getDriverId():String?{
        return mSharedPrefsHelper.driverId
    }
    fun setAdminId(adminId: String){
        mSharedPrefsHelper.adminId =adminId
    }
    fun getAdminId():String?{
        return mSharedPrefsHelper.adminId
    }

    fun setDriverOwnId(driverOwnId: String){
        mSharedPrefsHelper.driverOwnId =driverOwnId
    }
    fun getDriverOwnId():String?{
        return mSharedPrefsHelper.driverOwnId
    }

    fun setDriverType(driverType: String){
        mSharedPrefsHelper.driverType =driverType
    }
    fun getDriverType():String?{
        return mSharedPrefsHelper.driverType
    }

    fun setIsBlocked(isBlocked: String){
        mSharedPrefsHelper.isBlocked =isBlocked
    }
    fun getIsBlocked():String?{
        return mSharedPrefsHelper.isBlocked
    }
    fun setVehicleId(vehicleId: String){
        mSharedPrefsHelper.vehicleId =vehicleId
    }
    fun getVehicleId():String?{
        return mSharedPrefsHelper.vehicleId
    }

    fun setLanguage(language: String){
        mSharedPrefsHelper.language =language
    }
    fun getLanGuage():String?{
        return mSharedPrefsHelper.language
    }


    fun setBookingconfirmId(bookingid: String){
        mSharedPrefsHelper.bookingid =bookingid
    }
    fun getBookingConfirmId():String?{
        return mSharedPrefsHelper.bookingid
    }
}