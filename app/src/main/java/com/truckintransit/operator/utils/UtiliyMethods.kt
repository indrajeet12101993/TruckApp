package com.truckintransit.operator.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.content.pm.PackageManager.GET_META_DATA
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.truckintransit.operator.R
import com.truckintransit.operator.callbackInterface.ListnerForDialog
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

object UtiliyMethods {


    fun hideKeyboard(context: Context, view: View) {
        val v = (context as Activity).currentFocus
        if (v != null) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    fun showKeyboard(context: Context, view: View) {
        try {
            val input = context
                    .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            input.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        } catch (e: Exception) {
            e.printStackTrace()

        }
    }

    fun showDialogwithDismiss(context: Context,message:String) {
        val alertDialog = AlertDialog.Builder(context).create()
       // alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                DialogInterface.OnClickListener {
                    dialog, which ->

                    dialog.dismiss() })
        alertDialog.show()
    }

    fun showDialogwithMessage(context: Context,message:String,listnerForDialog: ListnerForDialog) {
        val alertDialog = AlertDialog.Builder(context).create()
        // alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
            DialogInterface.OnClickListener {
                    dialog, which ->
                dialog.dismiss()
                listnerForDialog.selctok()

                })
        alertDialog.show()
    }

    /**
     * Method checks if the app is in background or not
     */
    fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            val runningProcesses = am.runningAppProcesses
            for (processInfo in runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (activeProcess in processInfo.pkgList) {
                        if (activeProcess == context.getPackageName()) {
                            isInBackground = false
                        }
                    }
                }
            }
        } else {
            val taskInfo = am.getRunningTasks(1)
            val componentInfo = taskInfo[0].topActivity
            if (componentInfo.packageName == context.getPackageName()) {
                isInBackground = false
            }
        }

        return isInBackground
    }
    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    fun getBitmapFromURL(strURL: String): Bitmap? {
        try {
            val url = URL(strURL)
            val connection = url.openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input = connection.getInputStream()
            return BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

    }



     fun resetActivityTitle(a: Activity) {
        try {
            val info = a.packageManager.getActivityInfo(a.componentName, GET_META_DATA)
            if (info.labelRes != 0) {
                a.setTitle(info.labelRes)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

    }

    fun isAtLeastVersion(version: Int): Boolean {

        return Build.VERSION.SDK_INT >= version
    }













}