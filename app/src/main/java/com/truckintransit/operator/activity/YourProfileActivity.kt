package com.truckintransit.operator.activity


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.truckintransit.operator.R
import com.truckintransit.operator.base.BaseActivity
import com.truckintransit.operator.base.BaseApplication
import com.truckintransit.operator.dataprefence.DataManager
import com.truckintransit.operator.newtworking.ApiRequestClient
import com.truckintransit.operator.pojo.ResponseFromServerGeneric
import com.truckintransit.operator.pojo.adminDash.ResponseFromServerAdminDashBoard
import com.truckintransit.operator.pojo.driverDash.ResponseFromServerDriverDshBoard
import com.truckintransit.operator.utils.Validation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_choose_language.*
import kotlinx.android.synthetic.main.activity_your_profile.*

import android.content.Context
import android.view.inputmethod.InputMethodManager

import android.view.Menu


class YourProfileActivity : BaseActivity(){

    private var mCompositeDisposable: CompositeDisposable? = null
    lateinit var dataManager: DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_profile)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.title = getString(R.string.your_profile)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.yellow))

        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()

        if (dataManager.getDriverType() == "2") {
            startLoadingDriverDashBoard(dataManager.getDriverOwnId()!!)

            et_number.isEnabled=false
            et_email.isEnabled=false
            tv_username.isEnabled=false


        } else {
            et_number.isEnabled=true
            et_email.isEnabled=true
            tv_username.isEnabled=true
            startLoadingAdminDashBoard(dataManager.getAdminId()!!)
        }

        tv_updatePassword.setOnClickListener {
            val password = etpassword.text.toString()
            val username = tv_username.text.toString()
            val number = et_number.text.toString()
            val email = et_email.text.toString()
            if (!validation(password)) {

                if (dataManager.getDriverType() == "2") {
                    updatePasswordDriver(dataManager.getDriverOwnId()!!,password)

                } else {

                    updatePasswordAdmin(dataManager.getAdminId()!!,password,username,number,email)

                }


            }
        }






    }

   override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.seetingmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            android.R.id.home -> {
                onBackPressed()
                return true
            }
           R.id.add_seeting -> {
                 launchActivity<SeetingActivity>()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    //geeting  number for admin DashBoard
    private fun startLoadingAdminDashBoard(id: String) {


        showDialogLoading()

        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServerAdmin(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_Admin, this::handleError_Admin)
        )
    }

    // handle sucess response of api call of get phone number
    private fun handleResponse_Admin(response: ResponseFromServerAdminDashBoard) {
        hideDialogLoading()

        mCompositeDisposable?.clear()

        tv_username.setText(response.Basic_info[0].name)
        et_number.setText(response.Basic_info[0].mobile)
        et_email.setText(response.Basic_info[0].email)


        if(!response.Basic_info[0].image.isEmpty())
            Glide.with(this).load(response.Basic_info[0].image).into(imageView)



    }


    // handle failure response of api call of get phone number
    private fun handleError_Admin(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }


    //geeting  number for driver DashBoard
    private fun startLoadingDriverDashBoard(id: String) {


        showDialogLoading()

        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServerDriverDashBoard(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_Driver, this::handleError_Driver)
        )
    }

    // handle sucess response of api call of get phone number
    private fun handleResponse_Driver(response: ResponseFromServerDriverDshBoard) {
        hideDialogLoading()
        mCompositeDisposable?.clear()


        tv_username.setText(response.Basic_info[0].name)
        et_number.setText(response.Basic_info[0].mobile_no)
        et_email.setText(response.Basic_info[0].email)




        if(!response.Basic_info[0].driver_pic.isEmpty())
            Glide.with(this).load(response.Basic_info[0].driver_pic).into(imageView)


    }


    // handle failure response of api call of get phone number
    private fun handleError_Driver(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }




    private fun updatePasswordDriver(id: String, password: String ) {


        showDialogLoading()

        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .updatePasswordDriver(id,password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_paasdriver, this::paasdriver)
        )
    }

    // handle sucess response of api call of get phone number
    private fun handleResponse_paasdriver(response: ResponseFromServerGeneric) {
        hideDialogLoading()
        mCompositeDisposable?.clear()
        showDialogWithDismiss(response.response_message)




    }


    // handle failure response of api call of get phone number
    private fun paasdriver(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }


    private fun updatePasswordAdmin(id: String, password: String, username: String, number: String, email: String) {


        showDialogLoading()

        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .updatePasswordAdmin(id,password,username,email,number )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_paasadmin, this::paasadmin)
        )
    }

    // handle sucess response of api call of get phone number
    private fun handleResponse_paasadmin(response: ResponseFromServerGeneric) {
        hideDialogLoading()
        mCompositeDisposable?.clear()
        showDialogWithDismiss(response.response_message)




    }


    // handle failure response of api call of get phone number
    private fun paasadmin(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }





    private fun validation(otpnumber: String): Boolean {


        if (Validation.isEmptyField(otpnumber)) {
            etpassword.error = getString(R.string.error_no_text)
            etpassword.requestFocus()
            return true
        }

        return false


    }



}
