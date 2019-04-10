package com.truckintransit.operator.utils

import android.text.TextUtils


object Validation {

    fun isEmptyField(name: String): Boolean {

        return TextUtils.isEmpty(name)
    }

    fun isValidPhoneNumber(number: String): Boolean {
        if(number.length==10){
            return false
        }
        return true



    }

    fun isValidEmail(target: String): Boolean {
        return  !android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun isLengthCheckForOtp(name: String): Boolean {

        if(name.length>=6){
           return false
        }
        return true
    }

    fun isLengthCheckForPan(name: String): Boolean {

        if(name.length>=10){
            return false
        }
        return true
    }
    fun isLengthCheckForAdhar(name: String): Boolean {

        if(name.length>=12){
            return false
        }
        return true
    }
}