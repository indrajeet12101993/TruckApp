package com.truckintransit.operator.activity


import android.os.Bundle
import android.view.MenuItem
import com.truckintransit.operator.R
import com.truckintransit.operator.base.BaseActivity
import com.truckintransit.operator.base.BaseApplication
import com.truckintransit.operator.dataprefence.DataManager
import com.truckintransit.operator.newtworking.ApiRequestClient
import com.truckintransit.operator.pojo.otpVerify.ResponseFromServerOtpVerify
import com.truckintransit.operator.utils.Validation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {
    private var mBussinesspassword: String? = null
    private var mBussinessemail: String? = null
    private var mCompositeDisposable: CompositeDisposable? = null
    lateinit var dataManager: DataManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar_title.text=getString(R.string.login)
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()

        loginbutton.setOnClickListener {

            if(!validationForLogin()){
                getLogin()

            }


        }
        tv_terms.setOnClickListener {
            launchActivity<WebViewActivity>()
        }
        tvprivacy.setOnClickListener {
            launchActivity<WebViewActivity>()
        }



    }

    private fun validationForLogin(): Boolean {


        mBussinesspassword = et_password.text.toString()
        mBussinessemail = et_email.text.toString()
        if (Validation.isEmptyField(mBussinessemail!!)) {
            et_email.error = getString(R.string.error_no_text)
            et_email.requestFocus()
            return true

        }
        if (Validation.isEmptyField(mBussinesspassword!!)) {
            et_password.error = getString(R.string.error_no_text)
            et_password.requestFocus()
            return true

        }




        return false
    }

    private fun getLogin() {

        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postLogin(mBussinessemail!!,mBussinesspassword!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_otp, this::handleError_otp)
        )

    }


    private fun handleResponse_otp(responseFromServerOtpVerify: ResponseFromServerOtpVerify) {
        hideDialogLoading()
        mCompositeDisposable?.clear()

        if (responseFromServerOtpVerify.response_code.equals("1")) {
            showDialogWithDismiss(getString(R.string.valid_otp))

        } else {

            if (responseFromServerOtpVerify.result.isNotEmpty()) {

                if (responseFromServerOtpVerify.result[0].type == "1") {
                    dataManager.setAdminId(responseFromServerOtpVerify.result[0].id)
                    dataManager.setDriverType(responseFromServerOtpVerify.result[0].type)
                    launchActivityWithFinish<DashBoardActvity>()
                }
                if (responseFromServerOtpVerify.result[0].type == "2") {

                    dataManager.setDriverOwnId(responseFromServerOtpVerify.result[0].id)
                    dataManager.setDriverType(responseFromServerOtpVerify.result[0].type)
                    launchActivityWithFinish<DashBoardActvity>()

                }
                if (responseFromServerOtpVerify.result[0].type == "3") {
                    dataManager.setAdminId(responseFromServerOtpVerify.result[0].id)
                    dataManager.setDriverOwnId(responseFromServerOtpVerify.result[0].driver_id)
                    dataManager.setDriverType(responseFromServerOtpVerify.result[0].type)
                    launchActivityWithFinish<DashBoardActvity>()


                }
            }
        }





    }


    private fun handleError_otp(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}
