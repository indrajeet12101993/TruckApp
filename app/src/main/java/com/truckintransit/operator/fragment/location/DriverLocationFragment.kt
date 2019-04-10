package com.truckintransit.operator.fragment.location

import android.Manifest
import android.app.PendingIntent
import android.os.Bundle
import com.truckintransit.operator.R
import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import com.truckintransit.operator.constants.AppConstants.REQUEST_LOCATION_PERMISSION
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.truckintransit.operator.backgroundJobs.FetchAddressTask
import com.truckintransit.operator.callbackInterface.OnTaskCompleted
import com.truckintransit.operator.constants.AppConstants.REQUEST_LOCATION_PERMISSION_LOCATION
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.GoogleMap
import com.truckintransit.operator.base.BaseApplication
import com.truckintransit.operator.base.BaseFragment
import com.truckintransit.operator.callbackInterface.ListnerForDialog
import com.truckintransit.operator.constants.AppConstants
import com.truckintransit.operator.constants.AppConstants.FindRiderId
import com.truckintransit.operator.constants.AppConstants.isonline
import com.truckintransit.operator.dataprefence.DataManager
import com.truckintransit.operator.newtworking.ApiRequestClient
import com.truckintransit.operator.pojo.ResponseFromServerGeneric
import com.truckintransit.operator.service.LocationUpdatesIntentService
import com.truckintransit.operator.utils.UtiliyMethods
import io.reactivex.Observable

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_driver_location.view.*
import java.util.concurrent.TimeUnit


class DriverLocationFragment : BaseFragment(), OnMapReadyCallback, OnTaskCompleted, ListnerForDialog {
    override fun selctok() {
        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))


    }

    override fun onTaskCompleted(result: String) {
        diplaylocation(result)

    }

    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mMarker: Marker
    private var mIsUserOnline: Boolean = false
    private var mDriverId: Boolean = false
    private lateinit var mUserLatitude: String
    private lateinit var mUserOngitude: String
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLocationCallback: LocationCallback? = null
    private var mLastLocation: Location? = null
    private var mCompositeDisposable: CompositeDisposable? = null
    private lateinit var dataManager: DataManager
    private lateinit var relativemap: RelativeLayout
    private lateinit var animation1: LottieAnimationView
    private lateinit var tv_stautus: TextView
    private lateinit var switch_toogle: SwitchCompat
    private var disposable: Disposable? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_driver_location, container, false)
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()
        relativemap = view.relativemap
        animation1 = view.animation1
        tv_stautus = view.tv_stautus
        switch_toogle = view.switch_toogle
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        // Initialize the FusedLocationClient.
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)

        val locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        if (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Toast.makeText(activity, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show()
        } else {

            UtiliyMethods.showDialogwithMessage(activity!!, getString(R.string.gpsdisabled), this)


        }


        // Initialize the location callbacks.
        mLocationCallback = object : LocationCallback() {
            /**
             * This is the callback that is triggered when the
             * FusedLocationClient updates your location.
             * @param locationResult The result containing the device location.
             */
            override fun onLocationResult(locationResult: LocationResult?) {


                mLastLocation = locationResult!!.lastLocation
                //getPendingIntent()


                FetchAddressTask(activity!!, this@DriverLocationFragment).execute(locationResult.lastLocation)


            }
        }

        if (isonline) {
            tv_stautus.text = getString(R.string.online)
            switch_toogle.isChecked = true
            mIsUserOnline = true

            disposable = Observable.interval(1000, 5000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::calleverysecond, this::calleveryerror)


        } else {
            tv_stautus.text = getString(R.string.offfline)
            switch_toogle.isChecked = false
            mIsUserOnline = false
        }

        view.switch_toogle.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                tv_stautus.text = getString(R.string.online)
                showSnackBar(getString(R.string.online))
                mIsUserOnline = true
                isonline = true

                disposable = Observable.interval(1000, 5000, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::calleverysecond, this::calleveryerror)

            } else {
                mIsUserOnline = false
                isonline = false
                view.tv_stautus.text = getString(R.string.offfline)
                showSnackBar(getString(R.string.offfline))
                startSendingLatLong(FindRiderId, "", "", "2")
                stopTrackingLocation()
            }
        }


        startTrackingLocation()
        return view
    }

    private fun calleverysecond(long: Long) {

        startSendingLatLong(FindRiderId, mUserLatitude, mUserOngitude, "1")

    }

    private fun calleveryerror(throwable: Throwable) {

    }

    //geeting  number for transport
    private fun startSendingLatLong(driver_id: String, lat: String, longg: String, isonline: String) {


        // showDialogLoading()


        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServerUserLatLong(driver_id, lat, longg, isonline)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        )
    }

    // handle sucess response of api call of get transport
    private fun handleResponse(responseFromSerevr: ResponseFromServerGeneric) {
        // hideDialogLoading()
        mCompositeDisposable?.clear()


    }


    // handle failure response of api call of get transport
    private fun handleError(error: Throwable) {
        // hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }


    private fun diplaylocation(result: String) {

        relativemap.visibility = View.VISIBLE
        animation1.cancelAnimation()
        animation1.visibility = View.GONE

        val latitude = mLastLocation!!.latitude
        val longitude = mLastLocation!!.longitude
        mUserLatitude = mLastLocation!!.latitude.toString()
        mUserOngitude = mLastLocation!!.longitude.toString()
        //1-- online driver
        //2-- offline driver


        val home = LatLng(latitude, longitude)
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney),10)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(home, 17f))




        mMarker = mMap.addMarker(
            MarkerOptions().position(home).title(result).icon(
                bitmapDescriptorFromVector(
                    activity!!,
                    R.drawable.ic_trucking
                )
            )
        )


//        mMap.setOnCameraIdleListener{
//            //get latlng at the center by calling
//
//            val midLatLng = mMap.getCameraPosition().target
//            showSnackBar(midLatLng.toString())
//        }

        mMarker.showInfoWindow()
//        mMap.setOnCameraChangeListener {
//            val location = mMap.cameraPosition.target
//            val marker = MarkerOptions().position(location).title("")
//            mMap.clear()
//            mMap.addMarker(marker)
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16f))
//        }
        //  dropPinEffect(mMarker)


        // mMarker.isDraggable = true

        // rotateMarker(mMarker, -360f)

        enableMyLocation(true)
        mMap.setOnPoiClickListener {
            val poiMarker: Marker = mMap.addMarker(
                MarkerOptions()
                    .position(it.latLng)
                    .title(it.name)
            )
            poiMarker.showInfoWindow()
        }

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


    }


    private fun enableMyLocation(enable: Boolean) {
        if (ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = enable
        } else {
            ActivityCompat.requestPermissions(
                activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        // Check if location permissions are granted and if so enable the
        // location data layer.
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation(true)

            }
            REQUEST_LOCATION_PERMISSION_LOCATION -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startTrackingLocation()

            } else {
                showSnackBar("permission denied")
            }


        }
    }

    /**
     * Starts tracking the device. Checks for
     * permissions, and requests them if they aren't present. If they are,
     * requests periodic location updates, sets a loading text and starts the
     * animation.
     */
    private fun startTrackingLocation() {


        if (ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION_LOCATION
            )
        } else {
            mFusedLocationClient!!.requestLocationUpdates(getLocationRequest(), mLocationCallback, null)
           // mFusedLocationClient!!.requestLocationUpdates(getLocationRequest(), getPendingIntent())


        }


    }

    /**
     * Sets up the location request.
     *
     * @return The LocationRequest object containing the desired parameters.
     */
    private fun getLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest()
        locationRequest.interval = 100000
        locationRequest.fastestInterval = 50000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        return locationRequest
    }


    private fun getPendingIntent(): PendingIntent {

        val intent = Intent(activity, LocationUpdatesIntentService::class.java)
        intent.action = AppConstants.ACTION_PROCESS_UPDATES
        return PendingIntent.getService(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    override fun onPause() {

        super.onPause()
        /*  stopTrackingLocation()*/
    }

    override fun onStop() {
        super.onStop()
        /*stopTrackingLocation()
        disposable.dispose()*/
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        if (disposable != null) {
            disposable?.dispose()
        }
        stopTrackingLocation()
        super.onDestroy()


    }


    /**
     * Stops tracking the device. Removes the location
     * updates, stops the animation, and resets the UI.
     */
    private fun stopTrackingLocation() {
        // mMarker.remove()
        mMap.clear()
        enableMyLocation(false)
        mFusedLocationClient!!.removeLocationUpdates(mLocationCallback)
        // startSendingLatLong(FindRiderId,"","","2")

    }


    private fun bitmapDescriptorFromVector(context: Context, vectorDrawableResourceId: Int): BitmapDescriptor {
        val background: Drawable = ContextCompat.getDrawable(context, R.drawable.ic_trucking)!!
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight())
        val vectorDrawable: Drawable = ContextCompat.getDrawable(context, vectorDrawableResourceId)!!
//       // vectorDrawable.setBounds(
//            40,
//            20,
//            vectorDrawable.getIntrinsicWidth() + 40,
//            vectorDrawable.getIntrinsicHeight() + 20
//        );
        val bitmap: Bitmap = Bitmap.createBitmap(
            background.getIntrinsicWidth(),
            background.getIntrinsicHeight(),
            Bitmap.Config.ARGB_8888
        );
        val canvas: Canvas = Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onDetach() {
        super.onDetach()

//        val builder = AlertDialog.Builder(activity!!)
//
//
//        builder.setTitle("App background color")
//        builder.setMessage("Are you want to set the app background color to RED?")
//
//
//        builder.setPositiveButton("YES"){dialog, which ->
//            dialog.dismiss()
//
//        }
//
//        builder.setNegativeButton("No"){dialog,which ->
//
//        }
//
//
////        // Display a neutral button on alert dialog
////        builder.setNeutralButton("Cancel"){_,_ ->
////            Toast.makeText(applicationContext,"You cancelled the dialog.",Toast.LENGTH_SHORT).show()
////        }
//
//
//        val dialog: AlertDialog = builder.create()
//
//
//        dialog.show()
    }

}




