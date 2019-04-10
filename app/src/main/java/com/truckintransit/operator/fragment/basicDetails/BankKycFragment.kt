package com.truckintransit.operator.fragment.basicDetails


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.textfield.TextInputEditText
import com.truckintransit.operator.R
import com.truckintransit.operator.activity.DashBoardActvity
import com.truckintransit.operator.base.BaseApplication
import com.truckintransit.operator.base.BaseFragment
import com.truckintransit.operator.callbackInterface.ListnerForDialog
import com.truckintransit.operator.dataprefence.DataManager
import com.truckintransit.operator.newtworking.ApiRequestClient
import com.truckintransit.operator.pojo.ResponseFromServerGeneric
import com.truckintransit.operator.utils.UtiliyMethods
import com.truckintransit.operator.utils.Validation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_bank_kyc.view.*


class BankKycFragment : BaseFragment(), ListnerForDialog {
    private lateinit var addhar_opt: String
    private lateinit var pan_number: String
    private lateinit var bank_account_optional: String
    private lateinit var ifsc_opt: String
    private lateinit var et_addhar_opt: TextInputEditText
    private lateinit var et_pan_number: TextInputEditText
    private lateinit var et_bank_account_optional: TextInputEditText
    private lateinit var et_ifsc_opt: TextInputEditText
    private var mCompositeDisposable: CompositeDisposable? = null

    private lateinit var dataManager: DataManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_bank_kyc, container, false)
        et_addhar_opt = view.et_addhar_opt
        et_pan_number = view.et_pan_number
        et_bank_account_optional = view.et_bank_account_optional
        et_ifsc_opt = view.et_ifsc_opt
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()

        view.tv_next.setOnClickListener {
            if(!validationForTransport()){

                startRegistrationForVehicle(dataManager.getAdminId()!!,addhar_opt,pan_number,bank_account_optional,ifsc_opt)

            }



        }

        return view
    }

    override fun selctok() {
        launchActivityWithFinish(DashBoardActvity())
           activity!!.finish()
    }

    //geeting  number for transport
    private fun startRegistrationForVehicle(
        id: String,
        addhar_opt: String,
        pan_number: String,
        bank_account_optional: String,
        ifsc_opt: String
    ) {


        showDialogLoading()

        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServerBankRegistrstion(id,addhar_opt,pan_number,bank_account_optional,ifsc_opt,"true")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        )
    }

    // handle sucess response of api call of get transport
    private fun handleResponse(responseFromSerevr: ResponseFromServerGeneric) {
        hideDialogLoading()
        UtiliyMethods.showDialogwithMessage(activity!!,responseFromSerevr.response_message,this )
        mCompositeDisposable?.clear()


    }


    // handle failure response of api call of get transport
    private fun handleError(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }

    private fun validationForTransport():Boolean {

        addhar_opt = et_addhar_opt.text.toString()
        pan_number = et_pan_number.text.toString()
        bank_account_optional = et_bank_account_optional.text.toString()
        ifsc_opt =  et_ifsc_opt .text.toString()

        if(Validation.isEmptyField(addhar_opt)){
            et_addhar_opt.error = getString(R.string.error_no_text)
            et_addhar_opt.requestFocus()
            return true

        }
        if(Validation.isLengthCheckForAdhar(addhar_opt)){
            et_addhar_opt.error = getString(R.string.adhar_length)
            et_addhar_opt.requestFocus()
            return true

        }

        if(Validation.isEmptyField(pan_number)){
            et_pan_number.error = getString(R.string.error_no_text)
            et_pan_number.requestFocus()
            return true

        }
        if(Validation.isLengthCheckForPan(pan_number)){
            et_pan_number.error = getString(R.string.panlength)
            et_pan_number.requestFocus()
            return true

        }



        return false
    }


}
