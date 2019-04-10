package com.truckintransit.operator.fragment.vehicleEdit


import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.content.Intent
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
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
import com.truckintransit.operator.constants.AppConstants
import com.truckintransit.operator.dataprefence.DataManager
import com.truckintransit.operator.newtworking.ApiRequestClient
import com.truckintransit.operator.pojo.ResponseFromServerGeneric
import com.truckintransit.operator.pojo.catogoryDilaog.ResponseForCatogoryDialog
import com.truckintransit.operator.pojo.vehicleDetailList.ResponseFromServerVehicleDetailList
import com.truckintransit.operator.utils.FileHelper
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


class AddVehicleBasicInfoEditFragment : BaseFragment(), ListnerForDialog, InterfaceCustomDialogCatogoryListner {
    private lateinit var et_vehicle_brand_name: TextInputEditText
    private lateinit var et_vehicle_model: TextInputEditText
    private lateinit var et_registor_no: TextInputEditText
    private lateinit var et_colour: TextInputEditText
    private lateinit var et_size: TextInputEditText
    private lateinit var et_capacity: TextInputEditText
    private lateinit var et_body_type: TextInputEditText
    private lateinit var et_select_feild_type: TextInputEditText
    private lateinit var et_select_vehicle: TextInputEditText
    private lateinit var imageView: ImageView
    private  var IsprofilePhoto:Boolean?=false
    private lateinit var dataManager: DataManager
    private var mCompositeDisposable_update_Photo: CompositeDisposable? = null
    private var mCompositeDisposable: CompositeDisposable? = null
    private lateinit var mFileUserPhoto: File
    private lateinit var dialog_custom: Dialog

    private lateinit var vehicle_brand_name: String
    private lateinit var vehicle_model: String
    private lateinit var registor_no: String
    private lateinit var colour: String
    private lateinit var size: String
    private lateinit var iSCatogorType: String
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




        startLoadingForDriverDetails()
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
    private fun replaceFragment(fragment: androidx.fragment.app.Fragment) {

        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.place_holder_for_fragment, fragment)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.addToBackStack(null)
        transaction.commit()
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
        editingUserPhoto()

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


        mCompositeDisposable_update_Photo = CompositeDisposable()

        mCompositeDisposable_update_Photo?.add(
            ApiRequestClient.createREtrofitInstance()
                .editVehicleBasicDetails( AppConstants.VEHICLEDETAILID, vehicle_brand_name, vehicle_model, registor_no,capacity_id,
                    colour,size,body_id,fuel_id,vehicle_id, "true",capacity_name,body_name,fuel_name,vehicle_name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseUser_Photo, this::handleError_user_photo)
        )
    }


    // handle sucess response of api call registration
    private fun handleResponseUser_Photo(response: ResponseFromServerGeneric) {
        hideDialogLoading()
        mCompositeDisposable_update_Photo?.clear()
        if (response.response_code == "0") {

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
        replaceFragment(AddVehicleBasicInfroTwoEditFragment())
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
    //geeting  number for get individual driver details
    private fun startLoadingForDriverDetails() {


        showDialogLoading()

        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServerVehicleBasiCDetails(AppConstants.VEHICLEDETAILID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        )
    }

    // handle sucess response of api call of get phone number
    private fun handleResponse(response: ResponseFromServerVehicleDetailList) {
        hideDialogLoading()


        if(response.vehicle_detail.size>0) {

           et_vehicle_brand_name.setText(response.vehicle_detail[0].brand)
           et_vehicle_model.setText(response.vehicle_detail[0].model)
           et_registor_no.setText(response.vehicle_detail[0].reg_no)
           et_colour.setText(response.vehicle_detail[0].color)
           et_size.setText(response.vehicle_detail[0].size)
           et_capacity.setText(response.vehicle_detail[0].capacity_name)
           capacity_id = response.vehicle_detail[0].capacity
           et_body_type.setText(response.vehicle_detail[0].body_name)
           body_id = response.vehicle_detail[0].body
           et_select_feild_type.setText(response.vehicle_detail[0].fuel_name)
           fuel_id = response.vehicle_detail[0].fuel_type
           et_select_vehicle.setText(response.vehicle_detail[0].select_vehiclename)
           vehicle_id = response.vehicle_detail[0].select_vehicle
            Glide.with(activity!!).load(response.vehicle_detail[0].vehicle_photo).into(imageView)
       }
        mCompositeDisposable?.clear()



    }


    // handle failure response of api call of get phone number
    private fun handleError(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }

    // api call for user registration
    private fun editingUserPhoto() {

        showDialogLoading()
        val file = FileHelper.reduceFileSize(mFileUserPhoto!!)
        val reqFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file!!)
        val body: MultipartBody.Part = MultipartBody.Part.createFormData("photo", file!!.name.toString(), reqFile)
        val id: RequestBody = RequestBody.create(MediaType.parse("text/plain"),AppConstants.VEHICLEDETAILID)


        mCompositeDisposable_update_Photo = CompositeDisposable()

        mCompositeDisposable_update_Photo?.add(
            ApiRequestClient.createREtrofitInstance()
                .editImageVehicleProfile(body, id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseEdit_Photo, this::handleError_Edit_photo)
        )
    }


    // handle sucess response of api call registration
    private fun handleResponseEdit_Photo(response: ResponseFromServerGeneric) {
        hideDialogLoading()
        showDialogWithDismiss(response.response_message)
        mCompositeDisposable_update_Photo?.clear()

    }


    // handle failure response of api call registration
    private fun handleError_Edit_photo(error: Throwable) {

        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable_update_Photo?.clear()

    }


}
