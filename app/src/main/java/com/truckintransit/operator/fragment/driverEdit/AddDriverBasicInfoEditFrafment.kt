package com.truckintransit.operator.fragment.driverEdit


import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout.HORIZONTAL
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.truckintransit.operator.R
import com.truckintransit.operator.adapter.CustomAdapterForCity
import com.truckintransit.operator.base.BaseApplication
import com.truckintransit.operator.base.BaseFragment
import com.truckintransit.operator.callbackInterface.InterfaceCitySelectListner
import com.truckintransit.operator.constants.AppConstants
import com.truckintransit.operator.dataprefence.DataManager
import com.truckintransit.operator.newtworking.ApiRequestClient
import com.truckintransit.operator.pojo.ResponseFromServerGeneric
import com.truckintransit.operator.pojo.citylist.ResponseFromServerCityList
import com.truckintransit.operator.pojo.citylist.Result
import com.truckintransit.operator.pojo.driverDetails.ResponseFromServerDriverDetails
import com.truckintransit.operator.utils.Validation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.city_list.*
import kotlinx.android.synthetic.main.fragment_add_driver_basic_info.view.*


class AddDriverBasicInfoEditFrafment : BaseFragment(), InterfaceCitySelectListner {

    override fun onItemClickState(result: Result) {
        dialog_city.dismiss()
        mCity= result.city_name
        mCityID=result.id
        et_registerd_city.setText(result.city_name)
        et_registerd_city.requestFocus()
    }

    private var mCompositeDisposable: CompositeDisposable? = null
    private lateinit var et_name_as_adhar: TextInputEditText
    private lateinit var et_mobile: TextInputEditText
    private lateinit var et_email: TextInputEditText
    private lateinit var et_registerd_city: TextInputEditText
    private lateinit var mNameAdhar: String
    private lateinit var mMobile: String
    private lateinit var mMailId: String
    private lateinit var mCity: String
    private lateinit var mCityID: String
    private lateinit var mDriverId: String
    private lateinit var dialog_city: Dialog
    private lateinit var dataManager: DataManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View= inflater.inflate(R.layout.fragment_add_driver_basic_info, container, false)
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()
        et_name_as_adhar = view.et_name_as_adhar
        et_mobile = view.et_mobile
        et_email = view.et_email
        et_registerd_city = view.et_registerd_city
        initCity()


        et_registerd_city.setOnClickListener {

            getCity()
        }
        view.tv_next.setOnClickListener {

            if(!validationForDriverBasicRegistration()){
                apiCallDriverBasicDetsils()
            }



        }
        startLoadingForDriverDetails()
        return view
    }


    // initiliaze city dialog
    private fun initCity() {
        dialog_city = Dialog(activity!!)
        dialog_city.setContentView(R.layout.city_list)
        dialog_city.setCanceledOnTouchOutside(false)
        dialog_city.setCancelable(true)
        dialog_city.setTitle(getString(R.string.select_city))
        dialog_city.recycler_view.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        val itemDecor = DividerItemDecoration(activity, HORIZONTAL)
        dialog_city.recycler_view.addItemDecoration(itemDecor)


    }

    private fun replaceFragment(fragment: Fragment) {

        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.place_holder_for_fragment, fragment)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.

            //inflater.inflate(R.menu.adddriver, menu)



    }

    private fun validationForDriverBasicRegistration():Boolean {

        mNameAdhar = et_name_as_adhar.text.toString()
        mMobile = et_mobile.text.toString()
        mMailId = et_email.text.toString()
        mCity = et_registerd_city.text.toString()


        if(Validation.isEmptyField(mNameAdhar)){
            et_name_as_adhar.error = getString(R.string.error_no_text)
            et_name_as_adhar.requestFocus()
            return true

        }
        if(Validation.isEmptyField(mMobile)){
            et_mobile.error = getString(R.string.error_no_text)
            et_mobile.requestFocus()
            return true

        }
        if(Validation.isValidPhoneNumber(mMobile)){
            et_mobile.error = getString(R.string.valid_phone_number)
            et_mobile.requestFocus()
            return true

        }
        if(Validation.isEmptyField(mMailId)){
            et_email.error = getString(R.string.error_no_text)
            et_email.requestFocus()
            return true

        }
        if(Validation.isValidEmail(mMailId)){
            et_email.error = getString(R.string.valid_mail_adress)
            et_email.requestFocus()
            return true

        }
        if(Validation.isEmptyField(mCity)){
            et_registerd_city.error = getString(R.string.error_no_text)
            et_registerd_city.requestFocus()
            return true

        }


        return false


    }




    // api call for get city
    private fun getCity() {


        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .getCityList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_city, this::handleError_City))
    }


    // handle sucess response of api call
    private fun handleResponse_city(responseFromServerCity: ResponseFromServerCityList) {
        hideDialogLoading()

        val result = responseFromServerCity.result
        if (result.isNotEmpty()) {
            val mCityAdapter = CustomAdapterForCity(result, this)
            dialog_city.recycler_view.adapter = mCityAdapter
            dialog_city.show()


        }
        mCompositeDisposable?.clear()


    }


    // handle failure response of api call
    private fun handleError_City(error: Throwable) {
        mCompositeDisposable?.clear()
        hideDialogLoading()
    }

    // api call for post server basic registartion update  driver
    private fun apiCallDriverBasicDetsils() {


        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postUdtaeDriver(mDriverId,mNameAdhar,mMobile,mMailId,mCityID,mCity,"true")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_Basic_registrayion, this::handleError_Basic_registrayion_error))
    }


    // handle sucess response of api call
    private fun handleResponse_Basic_registrayion(response: ResponseFromServerGeneric) {
        hideDialogLoading()

        //0-- suuces
        //1-- mobile already exists

        if(response.response_code=="1"){
            showSnackBar(response.response_message)
        }else{
            if (response.response_message.isNotEmpty()) {

                replaceFragment(AddDriverBasicInfoTwoEditFragment())

            }
        }

        mCompositeDisposable?.clear()


    }


    // handle failure response of api call
    private fun handleError_Basic_registrayion_error(error: Throwable) {
        mCompositeDisposable?.clear()
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
    }


    //geeting  number for get individual driver details
    private fun startLoadingForDriverDetails() {


        showDialogLoading()

        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServerDriverBasiCDetails(AppConstants.DRIVERID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        )
    }

    // handle sucess response of api call of get phone number
    private fun handleResponse(response: ResponseFromServerDriverDetails) {
        hideDialogLoading()
        mCompositeDisposable?.clear()

        mCityID=response.driver_detail[0].city_id
        mDriverId=response.driver_detail[0].id
        et_name_as_adhar.setText(response.driver_detail[0].name)
        et_mobile.setText(response.driver_detail[0].mobile_no)
        et_email.setText(response.driver_detail[0].email)
        et_registerd_city.setText(response.driver_detail[0].city_name)



    }


    // handle failure response of api call of get phone number
    private fun handleError(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }



}
