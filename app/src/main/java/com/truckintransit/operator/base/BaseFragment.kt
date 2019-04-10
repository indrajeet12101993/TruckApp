package com.truckintransit.operator.base

import android.app.Activity
import android.app.ActivityOptions
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.truckintransit.operator.R
import com.truckintransit.operator.utils.FileHelper
import com.truckintransit.operator.utils.UtiliyMethods
import java.io.File

abstract class BaseFragment : Fragment() {

    lateinit var dialog: ProgressDialog


    fun launchActivity(T: Activity) {

        val intent = Intent(activity, T::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Apply activity transition
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity!!).toBundle())
        } else {
            // Swap without transition
            startActivity(intent)
        }


    }



    fun isNetworkAvailable(): Boolean {
        val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }

    fun launchActivityWithFinish(T: Activity) {

        val intent = Intent(activity, T::class.java)
        startActivity(intent)
        activity!!.finish()

    }


    fun endActivity(T: Activity) {

        val intent = Intent(activity, T::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Apply activity transition
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity!!).toBundle())
        } else {
            // Swap without transition
            startActivity(intent)
        }


    }

    fun showDialogLoading() {

        dialog = ProgressDialog(activity!!)
        dialog.setMessage("Please wait...")
        dialog.setTitle("Loading...")
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.isIndeterminate = true
        if (!dialog.isShowing) {
            dialog.show()
        }

    }

    fun hideDialogLoading() {


        if (dialog != null && dialog.isShowing)
            dialog.cancel()


    }

    protected fun hideKeyboard(view: View) {
        UtiliyMethods.hideKeyboard(activity!!, view)
    }


    fun showSnackBar(message: String) {
        val snackbar = Snackbar.make(
            activity!!.findViewById<View>(android.R.id.content),
            message, Snackbar.LENGTH_SHORT
        )
        val sbView = snackbar.view
        val textView = sbView
            .findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(ContextCompat.getColor(activity!!, R.color.colorAccent))
        snackbar.show()
    }


    fun showDialogWithDismiss(message: String) {
        UtiliyMethods.showDialogwithDismiss(activity!!, message)
    }






}