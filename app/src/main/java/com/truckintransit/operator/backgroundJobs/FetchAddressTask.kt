package com.truckintransit.operator.backgroundJobs

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.AsyncTask
import android.text.TextUtils
import android.util.Log
import com.truckintransit.operator.callbackInterface.OnTaskCompleted
import java.io.IOException
import java.util.*

class FetchAddressTask(var mContext: Context,var mListener: OnTaskCompleted) : AsyncTask<Location, Void, String>() {





    private val TAG = FetchAddressTask::class.java.simpleName

    override fun doInBackground(vararg params: Location): String {
        // Set up the geocoder
        val geocoder = Geocoder(mContext, Locale.getDefault())

        // Get the passed in location
        val location = params[0]
        var addresses: List<Address>? = null
        var resultMessage = ""

        try {
            addresses = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                // In this sample, get just a single address max adress 1 if 2 then it gives two adress
                1
            )
        } catch (ioException: IOException) {
            // Catch network or other I/O problems
           // resultMessage = mContext.getString(R.string.service_not_available)
            Log.e(TAG, resultMessage, ioException)
        } catch (illegalArgumentException: IllegalArgumentException) {
            // Catch invalid latitude or longitude values
          //  resultMessage = mContext.getString(R.string.invalid_lat_long_used)
            Log.e(
                TAG, resultMessage + ". " +
                        "Latitude = " + location.latitude +
                        ", Longitude = " +
                        location.longitude, illegalArgumentException
            )
        }

        // If no addresses found, print an error message.
        if (addresses == null || addresses.size == 0) {
            if (resultMessage.isEmpty()) {
             //   resultMessage = mContext.getString(R.string.no_address_found)
                Log.e(TAG, resultMessage)
            }
        } else {
            // If an address is found, read it into resultMessage
            val address = addresses[0]
            val addressParts = ArrayList<String>()

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread
            for (i in 0..address.maxAddressLineIndex) {
                addressParts.add(address.getAddressLine(i))
            }

            resultMessage = TextUtils.join(
                "\n",
                addressParts
            )

        }

        return resultMessage
    }

    /**
     * Called once the background thread is finished and updates the
     * UI with the result.
     * @param address The resulting reverse geocoded address, or error
     * message if the task failed.
     */
    override fun onPostExecute(address: String) {
        mListener.onTaskCompleted(address)
        super.onPostExecute(address)
    }


}