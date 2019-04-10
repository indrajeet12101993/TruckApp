package com.truckintransit.operator.fragment.vehicleadd


import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle

import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

import com.truckintransit.operator.R
import com.truckintransit.operator.adapter.CustomAdapterForCustomDialogCatogary
import com.truckintransit.operator.base.BaseApplication
import com.truckintransit.operator.base.BaseFragment
import com.truckintransit.operator.callbackInterface.InterfaceCustomDialogCatogoryListner
import com.truckintransit.operator.callbackInterface.ListnerForDialog
import com.truckintransit.operator.dataprefence.DataManager
import com.truckintransit.operator.newtworking.ApiRequestClient
import com.truckintransit.operator.pojo.ResponseFromServerAddVehicle
import com.truckintransit.operator.pojo.catogoryDilaog.ResponseForCatogoryDialog
import com.truckintransit.operator.utils.FileHelper
import com.truckintransit.operator.utils.FileHelper.reduceFileSize
import com.truckintransit.operator.utils.UtiliyMethods
import com.truckintransit.operator.utils.Validation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.city_list.*
import kotlinx.android.synthetic.main.fragment_add_vehicle_basic_info.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class AddVehicleBasicInfoFragment : BaseFragment(), ListnerForDialog, InterfaceCustomDialogCatogoryListner {


    private lateinit var et_vehicle_brand_name: TextInputEditText
    private lateinit var et_vehicle_model: TextInputEditText
    private lateinit var et_registor_no: TextInputEditText
    private lateinit var et_colour: TextInputEditText
    private lateinit var et_size: TextInputEditText
    private lateinit var et_capacity: TextInputEditText
    private lateinit var et_body_type: TextInputEditText
    private lateinit var et_select_feild_type: TextInputEditText
    private lateinit var et_select_vehicle: TextInputEditText
    private lateinit var vehicle_brand_name: String
    private lateinit var vehicle_model: String
    private lateinit var registor_no: String
    private lateinit var colour: String
    private lateinit var size: String
    private lateinit var capacity: String
    private lateinit var body_type: String
    private lateinit var select_feild_type: String
    private lateinit var iSCatogorType: String
    private lateinit var select_vehicle: String
    private lateinit var imageView: ImageView
    private  var IsprofilePhoto:Boolean?=false
    private lateinit var dataManager: DataManager
    private var mCompositeDisposable_update_Photo: CompositeDisposable? = null
    private var mCompositeDisposable: CompositeDisposable? = null
    private lateinit var mFileUserPhoto: File
    private lateinit var dialog_custom: Dialog
    private lateinit var capacity_id: String
    private lateinit var capacity_name: String
    private lateinit var body_name: String
    private lateinit var body_id: String
    private lateinit var fuel_name: String
    private lateinit var fuel_id: String
    private lateinit var vehicle_name: String
    private lateinit var vehicle_id: String

    override fun funOnItemClick(result: com.truckintransit.operator.pojo.catogoryDilaog.Result) {
        dialog_custom.dismiss()

        if(iSCatogorType=="capacity"){
            capacity_name= result.name
            capacity_id=result.id
            et_capacity.setText(result.name)
            et_capacity.requestFocus()
        }
        if(iSCatogorType=="body"){
            body_name= result.name
            body_id=result.id
            et_body_type.setText(result.name)
            et_body_type.requestFocus()
        }
        if(iSCatogorType=="fuel"){
            fuel_name= result.name
            fuel_id=result.id
            et_select_feild_type.setText(result.name)
            et_select_feild_type.requestFocus()
        }
        if(iSCatogorType=="vehicle"){
            vehicle_name= result.name
            vehicle_id=result.id
            et_select_vehicle.setText(result.name)
            et_select_vehicle.requestFocus()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance=true
      //  setHasOptionsMenu(true)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View= inflater.inflate(R.layout.fragment_add_vehicle_basic_info, container, false)
        imageView=view.imageView
        et_vehicle_brand_name=view.et_vehicle_brand_name
        et_vehicle_model=view.et_vehicle_model
        et_registor_no=view.et_registor_no
        et_colour=view.et_colour
        et_size=view.et_size
        et_capacity=view.et_capacity
        et_body_type=view.et_body_type
        et_select_feild_type=view.et_select_feild_type
        et_select_vehicle=view.et_select_vehicle
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()
        initCustomDialog()
        view.tv_partner.setOnClickListener {

           // replaceFragment(AddVehicleBasicInfroTwoFragment())
            if(!validationForAddVehicle()){
                uploadingUserPhoto()
            }

        }
        imageView.setOnClickListener {
            startPic(4, 4)
        }

        et_capacity.setOnClickListener {
            dialog_custom.setTitle(getString(R.string.select_capicity))
            getCatogoryCapicity()
            iSCatogorType="capacity"
        }
        et_body_type.setOnClickListener {
            dialog_custom.setTitle(getString(R.string.select_body))
            getCatogoryBody()
            iSCatogorType="body"
        }
        et_select_feild_type.setOnClickListener {
            dialog_custom.setTitle(getString(R.string.select_fuel_type))
             getCatogorFuel()
            iSCatogorType="fuel"
        }
        et_select_vehicle.setOnClickListener {
            dialog_custom.setTitle(getString(R.string.select_vehicle_type))
             getCatogorVehicle()
            iSCatogorType="vehicle"
        }




        return view
    }

    // initiliaze custom dialog
    private fun initCustomDialog() {
        dialog_custom = Dialog(activity!!)
        dialog_custom.setContentView(R.layout.city_list)
        dialog_custom.setCanceledOnTouchOutside(false)
        dialog_custom.setCancelable(true)
        dialog_custom.recycler_view.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        val itemDecor = DividerItemDecoration(activity, LinearLayout.HORIZONTAL)
        dialog_custom.recycler_view.addItemDecoration(itemDecor)


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
       // inflater.inflate(R.menu.adddriver, menu)

    }

    private fun startPic(aspectRatioX: Int, aspectRatioY: Int) {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setActivityTitle("Crop")

            .setCropMenuCropButtonTitle("Done")
            .setCropMenuCropButtonIcon(R.drawable.ic_green_right_rounded)

            .start(context!!, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri

                onImageResult(File(FileHelper.getPath(activity!!, resultUri)))
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    private fun onImageResult(file: File) {
        IsprofilePhoto=true
        Glide.with(this).load(file).into(imageView)
        mFileUserPhoto = file

    }

    private fun validationForAddVehicle():Boolean {
        vehicle_brand_name = et_vehicle_brand_name.text.toString()
        vehicle_model = et_vehicle_model.text.toString()
        registor_no = et_registor_no.text.toString()
        colour = et_colour.text.toString()
        size = et_size.text.toString()
        capacity_name = et_capacity.text.toString()
        body_name = et_body_type.text.toString()
        fuel_name = et_select_feild_type.text.toString()
        vehicle_name = et_select_vehicle.text.toString()


        if(Validation.isEmptyField(vehicle_brand_name)){
            et_vehicle_brand_name.error = getString(R.string.error_no_text)
            et_vehicle_brand_name.requestFocus()
            return true

        }
        if(Validation.isEmptyField(vehicle_model)){
            et_vehicle_model.error = getString(R.string.error_no_text)
            et_vehicle_model.requestFocus()
            return true

        }
        if(Validation.isEmptyField(registor_no)){
            et_registor_no.error = getString(R.string.error_no_text)
            et_registor_no.requestFocus()
            return true

        }
        if(Validation.isEmptyField(colour)){
            et_colour.error = getString(R.string.error_no_text)
            et_colour.requestFocus()
            return true

        }
        if(Validation.isEmptyField(size)){
            et_size.error = getString(R.string.error_no_text)
            et_size.requestFocus()
            return true

        }
        if(Validation.isEmptyField(capacity_name)){
            et_capacity.error = getString(R.string.error_no_text)
            et_capacity.requestFocus()
            return true

        }
        if(Validation.isEmptyField(body_name)){
            et_body_type.error = getString(R.string.error_no_text)
            et_body_type.requestFocus()
            return true

        }
        if(Validation.isEmptyField(fuel_name)){
            et_select_feild_type.error = getString(R.string.error_no_text)
            et_select_feild_type.requestFocus()
            return true

        }
        if(Validation.isEmptyField(vehicle_name)){
            et_select_vehicle.error = getString(R.string.error_no_text)
            et_select_vehicle.requestFocus()
            return true

        }


        if(!IsprofilePhoto!!){
            showDialogWithDismiss(getString(R.string.profileImage))
            return true
        }
        return false


    }


    // api call for user registration
    private fun uploadingUserPhoto() {
        vehicle_brand_name = et_vehicle_brand_name.text.toString()
        vehicle_model = et_vehicle_model.text.toString()
        registor_no = et_registor_no.text.toString()
        colour = et_colour.text.toString()
        size = et_size.text.toString()
        capacity_name = et_capacity.text.toString()
        body_name = et_body_type.text.toString()
        fuel_name = et_select_feild_type.text.toString()
        vehicle_name= et_select_vehicle.text.toString()
        showDialogLoading()
        val file = reduceFileSize(mFileUserPhoto!!)
        val reqFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file!!)
        val body: MultipartBody.Part = MultipartBody.Part.createFormData("photo", file!!.name.toString(), reqFile)
        val id: RequestBody = RequestBody.create(MediaType.parse("text/plain"),dataManager.getAdminId()!!)
        val vehicle_brand_name: RequestBody = RequestBody.create(MediaType.parse("text/plain"), vehicle_brand_name)
        val vehicle_model: RequestBody = RequestBody.create(MediaType.parse("text/plain"), vehicle_model)
        val registor_no: RequestBody = RequestBody.create(MediaType.parse("text/plain"), registor_no)
        val capacity: RequestBody = RequestBody.create(MediaType.parse("text/plain"), capacity_id)
        val colour: RequestBody = RequestBody.create(MediaType.parse("text/plain"), colour)
        val size: RequestBody = RequestBody.create(MediaType.parse("text/plain"), size)
        val body_type: RequestBody = RequestBody.create(MediaType.parse("text/plain"), body_id)
        val select_feild_type: RequestBody = RequestBody.create(MediaType.parse("text/plain"), fuel_id)
        val select_vehicle: RequestBody = RequestBody.create(MediaType.parse("text/plain"), vehicle_id)
        val type: RequestBody = RequestBody.create(MediaType.parse("text/plain"), "true")
        val capacity_name: RequestBody = RequestBody.create(MediaType.parse("text/plain"), capacity_name)
        val body_name: RequestBody = RequestBody.create(MediaType.parse("text/plain"), body_name)
        val fuel_name: RequestBody = RequestBody.create(MediaType.parse("text/plain"), fuel_name)
        val select_vehicle_name: RequestBody = RequestBody.create(MediaType.parse("text/plain"), vehicle_name)


        mCompositeDisposable_update_Photo = CompositeDisposable()

        mCompositeDisposable_update_Photo?.add(
            ApiRequestClient.createREtrofitInstance()
                .uploadVehicleBasicDetails(body, id, vehicle_brand_name, vehicle_model, registor_no,capacity,
                    colour,size,body_type,select_feild_type,select_vehicle, type,capacity_name,body_name,fuel_name,select_vehicle_name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseUser_Photo, this::handleError_user_photo)
        )
    }


    // handle sucess response of api call registration
    private fun handleResponseUser_Photo(response: ResponseFromServerAddVehicle) {
        hideDialogLoading()
        mCompositeDisposable_update_Photo?.clear()
        if (response.response_code == "0") {
            dataManager.setVehicleId(response.vehicle_id)
            UtiliyMethods.showDialogwithMessage(activity!!,response.response_message,this )



        }



    }


    // handle failure response of api call registration
    private fun handleError_user_photo(error: Throwable) {

        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable_update_Photo?.clear()

    }

    override fun selctok() {
         replaceFragment(AddVehicleBasicInfroTwoFragment())
    }

    // api call for get capiacity
    private fun getCatogoryCapicity() {


        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .getCapicityList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_catogory, this::handleError_catogory))
    }


    // handle sucess response of api call
    private fun handleResponse_catogory(response: ResponseForCatogoryDialog) {
        hideDialogLoading()

        val result = response.Result
        if (result.isNotEmpty()) {
            val mCityAdapter = CustomAdapterForCustomDialogCatogary(result, this)
            dialog_custom.recycler_view.adapter = mCityAdapter
            dialog_custom.show()


        }
        mCompositeDisposable?.clear()


    }


    // handle failure response of api call
    private fun handleError_catogory(error: Throwable) {
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()
        hideDialogLoading()
    }

    // api call for get body
    private fun getCatogoryBody() {


        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .getBodyList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_Body, this::handleError_Body))
    }


    // handle sucess response of api call
    private fun handleResponse_Body(response: ResponseForCatogoryDialog) {
        hideDialogLoading()

        val result = response.Result
        if (result.isNotEmpty()) {
            val mCityAdapter = CustomAdapterForCustomDialogCatogary(result, this)
            dialog_custom.recycler_view.adapter = mCityAdapter
            dialog_custom.show()


        }
        mCompositeDisposable?.clear()


    }


    // handle failure response of api call
    private fun handleError_Body(error: Throwable) {
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()
        hideDialogLoading()
    }

    // api call for get fuel
    private fun getCatogorFuel() {


        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .getFuelList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_Fuel, this::handleError_Fuel))
    }


    // handle sucess response of api call
    private fun handleResponse_Fuel(response: ResponseForCatogoryDialog) {
        hideDialogLoading()

        val result = response.Result
        if (result.isNotEmpty()) {
            val mCityAdapter = CustomAdapterForCustomDialogCatogary(result, this)
            dialog_custom.recycler_view.adapter = mCityAdapter
            dialog_custom.show()


        }
        mCompositeDisposable?.clear()


    }


    // handle failure response of api call
    private fun handleError_Fuel(error: Throwable) {
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()
        hideDialogLoading()
    }
    // api call for get vehicle
    private fun getCatogorVehicle() {


        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .getVehicleList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_vehicle, this::handleError_vehicle))
    }


    // handle sucess response of api call
    private fun handleResponse_vehicle(response: ResponseForCatogoryDialog) {
        hideDialogLoading()

        val result = response.Result
        if (result.isNotEmpty()) {
            val mCityAdapter = CustomAdapterForCustomDialogCatogary(result, this)
            dialog_custom.recycler_view.adapter = mCityAdapter
            dialog_custom.show()


        }
        mCompositeDisposable?.clear()


    }


    // handle failure response of api call
    private fun handleError_vehicle(error: Throwable) {
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()
        hideDialogLoading()
    }

}
