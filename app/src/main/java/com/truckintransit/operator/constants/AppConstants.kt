package com.truckintransit.operator.constants

object AppConstants {

    internal const val PREFILENAME = "TTUCKAPPPREFENCE"
    internal const val PREF_KEY_IS_LOGGED_IN = "PREF_KEY_IS_LOGGED_IN"
    internal const val PREF_KEY_USER_DETAILS = "PREF_KEY_USER_DETAILS"
    internal const val PREF_KEY_DRIVER_ID = "PREF_KEY_DRIVER_ID"
    internal const val PREF_KEY_DRIVER_OWN_ID = "PREF_KEY_DRIVER_OWN_ID"
    internal const val PREF_KEY_DRIVER_TYPE = "PREF_KEY_DRIVER_TYPE"
    internal const val PREF_KEY_IS_BLOCKED= "PREF_KEY_IS_BLOCKED"
    internal const val PREF_KEY_ADMIN_ID = "PREF_KEY_ADMIN_ID"
    internal const val PREF_KEY_VEHICLE_ID = "PREF_KEY_VEHICLE_ID"
    internal const val PREF_KEY_LANGUAGE = "PREF_KEY_LANGUAGE"
    internal const val PREF_KEY_bookingid = "PREF_KEY_bookingid"
    internal const val SPLASH_DELAY: Long = 3000L // 3 seconds
    internal const val BASE_URL:String="http://technowhizzit.com/Truckintransit/"
    internal const val INTENTOPENREGISTRATION: String="INTENTOPENREGISTRATION"
    internal const val REQUEST_LOCATION_PERMISSION: Int=1
    internal const val REQUEST_LOCATION_PERMISSION_LOCATION: Int=2
    internal const val TRACKING_LOCATION_KEY = "tracking_location"
    internal const val ISMENUENABLEDEDIT = "ISMENUENABLEDEDIT"
    internal const val ACOOUNTAPPROVEDCHANNELID = "ACOOUNTAPPROVEDCHANNELID"
    internal const val ACOOUNTAPPROVEDCHANNELNAME = "ACOOUNTAPPROVED"
    internal const val ACOOUNTAPPROVEDCHANNELDESCRIPTION = "ACOOUNTAPPROVED NOTIFICATIONS"
    internal const val INTENTCONSTANTBRODCASTACCOUNTAPPROVED = "INTENTCONSTANTBRODCASTACCOUNTAPPROVED"
    internal const val INTENTCONSTANTBRODCASTBooking= "INTENTCONSTANTBRODCASTBooking"
    internal var DRIVERID  = ""
    internal var VEHICLEDETAILID  = ""
    internal var FindRiderId  = ""

    //profile
    internal var ISDONE1  = "false"
    internal var ISDONE2  = "false"
    internal var ISDONE3  = "false"
    internal var ISDONE4  = "false"

    // vehicle
    internal var ISDONE5  = "false"
    internal var ISDONE6  = "false"
    internal var ISDONE7  = "false"
    internal var ISDONE8  = "false"
    internal var ISDONE9  = "false"
    internal var ISDONE10  = "false"


    //webview url
    internal const val TERMSURL= "https://truckintransit.com/terms-of-service"

    internal var isonline :Boolean= false


    internal val ACTION_PROCESS_UPDATES =  "com.google.android.gms.location.sample.backgroundlocationupdates.action" + ".PROCESS_UPDATES"


}