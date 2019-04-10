package com.truckintransit.operator.fragment.basicDetails


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.textfield.TextInputEditText
import com.truckintransit.operator.R
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
import kotlinx.android.synthetic.main.fragment_transport.view.*


class TransportFragment : BaseFragment() ,ListnerForDialog{


    private lateinit var company_name: String
    private lateinit var company_gst: String
    private lateinit var company_adress: String
    private lateinit var company_registration: String
    private lateinit var et_company_name: TextInputEditText
    private lateinit var et_company_gst: TextInputEditText
    private lateinit var et_company_adress: TextInputEditText
    private lateinit var et_company_registration: TextInputEditText
    private var mCompositeDisposable: CompositeDisposable? = null

    private lateinit var dataManager: DataManager
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_transport, container, false)
        et_company_name = view.et_company_name
        et_company_gst = view.et_company_gst
        et_company_adress = view.et_company_adress
        et_company_registration = view.et_company_registration

        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()



        view.tv_next.setOnClickListener {
            if(!validationForTransport()){
                startRegistrationForVehicle(dataManager.getAdminId()!!,company_name,company_gst,company_adress,company_registration)

            }




        }

        return view
    }
    override fun selctok() {
        replaceFragment(BankKycFragment())
    }

    //geeting  number for transport
    private fun startRegistrationForVehicle(id: String, company_name: String, company_gst: String, company_adress: String,
        company_registration: String
    ) {


        showDialogLoading()

        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServerTransportRegistrstion(id,company_name,company_gst,company_adress,company_registration,"true")
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

        company_name = et_company_name.text.toString()
        company_gst = et_company_gst.text.toString()
        company_adress = et_company_adress.text.toString()
        company_registration =  et_company_registration .text.toString()


        if(Validation.isEmptyField(company_name)){
            et_company_name.error = getString(R.string.error_no_text)
            et_company_name.requestFocus()
            return true

        }
        if(Validation.isEmptyField(company_adress)){
            et_company_adress.error = getString(R.string.error_no_text)
            et_company_adress.requestFocus()
            return true

        }

      return false
    }

    private fun replaceFragment(fragment: Fragment) {

        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.place_holder_for_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}
