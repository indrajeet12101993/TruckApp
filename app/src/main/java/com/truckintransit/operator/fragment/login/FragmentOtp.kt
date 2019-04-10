package com.truckintransit.operator.fragment.login


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.truckintransit.operator.R
import com.truckintransit.operator.base.BaseFragment
import com.truckintransit.operator.newtworking.ApiRequestClient
import com.truckintransit.operator.pojo.ResponseFromServerGeneric
import com.truckintransit.operator.viewmodel.SharedViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_fragment_otp.view.*
import android.content.IntentFilter
import android.util.Log
import android.widget.TextView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.truckintransit.operator.activity.DashBoardActvity
import com.truckintransit.operator.activity.LoginActivity
import com.truckintransit.operator.activity.RegistrationActvity
import com.truckintransit.operator.activity.WebViewActivity
import com.truckintransit.operator.base.BaseApplication
import com.truckintransit.operator.broadcastRecevier.SMSBroadcastReceiver
import com.truckintransit.operator.constants.AppConstants
import com.truckintransit.operator.dataprefence.DataManager
import com.truckintransit.operator.pojo.adminDash.ResponseFromServerAdminDashBoard
import com.truckintransit.operator.pojo.otpVerify.ResponseFromServerOtpVerify
import com.truckintransit.operator.utils.Validation


class FragmentOtp : BaseFragment() {
    private lateinit var mSharedViewModel: SharedViewModel
    private var mCompositeDisposable: CompositeDisposable? = null
    private val smsBroadcastReceiver by lazy { SMSBroadcastReceiver() }
    lateinit var dataManager: DataManager
    lateinit var mPhoneNumber: String
    lateinit var tv_number:TextView
    private var mToken: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSharedViewModel = activity?.run {
            ViewModelProviders.of(this).get(SharedViewModel::class.java)
        } ?: throw Exception("Invalid Activity")


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_fragment_otp, container, false)
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()
        tv_number=view.tv_number
        mSharedViewModel.inputNumber.observe(this, Observer<String> { item ->

            transfernumber(item)
        }
        )

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result!!.token
                transferToken(token)

            })

        view.tv_resend_sms.setOnClickListener {
            startLoadingForPhoneNumber(mPhoneNumber)
        }

        view.tv_login_with_passowrd.setOnClickListener {
            launchActivity(LoginActivity())
        }

        view.tv_terms.setOnClickListener {

            launchActivity(WebViewActivity())

        }
        view.tvprivacy.setOnClickListener {

        }



        view.relative_login.setOnClickListener {
            val otpnumber = view.txt_pin_entry.text.toString()
            if (!validationforOtp(otpnumber)) {
                // check for verify otp
                verifyOtp(otpnumber)
            } else {
                showDialogWithDismiss(getString(R.string.valid_otp))
            }


        }


        val client = SmsRetriever.getClient(activity!!)
        val retriever = client.startSmsRetriever()
        retriever.addOnSuccessListener {

            val listener = object : SMSBroadcastReceiver.Listener {
                override fun onSMSReceived(otp: String) {

                    val otpsplit = otp.split(":")
                    val otpsplit1 = otpsplit[1]
                    view.txt_pin_entry.setText(otpsplit1)


                }


                override fun onTimeOut() {


                }
            }
            smsBroadcastReceiver.injectListener(listener)
            activity!!.registerReceiver(smsBroadcastReceiver, IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION))
        }
        retriever.addOnFailureListener {
            //Problem to start listener
        }

        return view
    }

    private fun transferToken(token: String) {
        mToken = token
        startLoadingForPhoneNumber(mPhoneNumber)

    }

    private fun transfernumber(item: String?) {
        mPhoneNumber = item!!
        tv_number.text= getString(R.string.enter_4_digit_otp_number_to_proceed)+"  +91"+mPhoneNumber

    }

    private fun validationforOtp(otpnumber: String): Boolean {


        if (Validation.isEmptyField(otpnumber)) {
            return true
        }

        if (Validation.isLengthCheckForOtp(otpnumber)) {
            return true
        }

        return false


    }


    // api call for otp verify
    private fun verifyOtp(otp: String) {

        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServerUserOtpVerify(otp)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseOtp, this::handleErrorOtp)
        )


    }

    // handle sucess response of api call of otp verify
    private fun handleResponseOtp(responseFromServerOtpVerify: ResponseFromServerOtpVerify) {
        hideDialogLoading()
        mCompositeDisposable?.clear()

        //false-- not fill data
        // true-- fill
        // 1-- admin
        // 2-- driver
        // 3-- admin/driver
        if (responseFromServerOtpVerify.response_code.equals("1")) {
            showDialogWithDismiss(getString(R.string.valid_otp))

        } else {

            if (responseFromServerOtpVerify.result.isNotEmpty()) {

                if (responseFromServerOtpVerify.result[0].type == "1") {
                    dataManager.setAdminId(responseFromServerOtpVerify.result[0].id)
                    dataManager.setDriverType(responseFromServerOtpVerify.result[0].type)
                    nextFlowForCall(responseFromServerOtpVerify.result[0].id)
                }
                if (responseFromServerOtpVerify.result[0].type == "2") {

                    dataManager.setDriverOwnId(responseFromServerOtpVerify.result[0].id)
                    dataManager.setDriverType(responseFromServerOtpVerify.result[0].type)
                    launchActivityWithFinish(DashBoardActvity())

                }
                if (responseFromServerOtpVerify.result[0].type == "3") {
                    dataManager.setAdminId(responseFromServerOtpVerify.result[0].id)
                    dataManager.setDriverOwnId(responseFromServerOtpVerify.result[0].driver_id)
                    dataManager.setDriverType(responseFromServerOtpVerify.result[0].type)
                    nextFlowForCall(responseFromServerOtpVerify.result[0].id)


                }
            }
        }


    }


    // handle failure response of api call otp verify
    private fun handleErrorOtp(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }

    private fun nextFlowForCall(id: String) {

        startLoadingAdminDashBoard(id)

    }


    //geeting  number for otp api
    private fun startLoadingForPhoneNumber(phonenumber: String) {


        showDialogLoading()

        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServerUserPhoneNumber(phonenumber, mToken!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        )
    }

    // handle sucess response of api call of get phone number
    private fun handleResponse(responseFromSerevrPhoneNumber: ResponseFromServerGeneric) {
        hideDialogLoading()
        mCompositeDisposable?.clear()


    }


    // handle failure response of api call of get phone number
    private fun handleError(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

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

        if (response.Basic_info[0].reg_type == "true" &&
            response.Transaction_detail[0].trans_type == "true" &&
            response.bank_detail[0].bank_type == "true"
        ) {
            launchActivityWithFinish(DashBoardActvity())
            activity!!.finish()
        } else {

            if (response.Basic_info.isNotEmpty()) {
                if (response.Basic_info[0].reg_type == "false") {
                    launchActivityFragment(RegistrationActvity(), getString(R.string.registration))
                    return
                }

            }
            if (response.Transaction_detail.isNotEmpty()) {
                if (response.Transaction_detail[0].trans_type == "false") {
                    launchActivityFragment(RegistrationActvity(), getString(R.string.transport))
                    return
                }

            }
            if (response.bank_detail.isNotEmpty()) {
                if (response.bank_detail[0].bank_type == "false") {
                    launchActivityFragment(RegistrationActvity(), getString(R.string.bank))
                    return
                }

            }


        }


    }


    // handle failure response of api call of get phone number
    private fun handleError_Admin(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }
    // start  actvity for registration

    fun launchActivityFragment(T: Activity, string: String) {
        val intent = Intent(activity, T::class.java)
        intent.putExtra(AppConstants.INTENTOPENREGISTRATION, string)
        startActivity(intent)
        activity!!.finish()


    }


    override fun onDestroy() {
        super.onDestroy()
        activity!!.unregisterReceiver(smsBroadcastReceiver)
    }


}
