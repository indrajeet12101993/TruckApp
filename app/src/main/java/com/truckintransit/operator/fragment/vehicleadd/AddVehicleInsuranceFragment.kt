package com.truckintransit.operator.fragment.vehicleadd


import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

import com.truckintransit.operator.R
import com.truckintransit.operator.base.BaseApplication
import com.truckintransit.operator.base.BaseFragment
import com.truckintransit.operator.callbackInterface.ListnerForDialog
import com.truckintransit.operator.constants.AppConstants
import com.truckintransit.operator.dataprefence.DataManager
import com.truckintransit.operator.newtworking.ApiRequestClient
import com.truckintransit.operator.pojo.ResponseFromServerGeneric
import com.truckintransit.operator.utils.FileHelper
import com.truckintransit.operator.utils.UtiliyMethods
import com.truckintransit.operator.utils.Validation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_add_vehicle_insurance.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class AddVehicleInsuranceFragment : BaseFragment() , ListnerForDialog {



    private lateinit var et_select_company_name: TextInputEditText
    private lateinit var et_insurancedate: TextInputEditText
    private lateinit var mCompanyNmae: String
    private lateinit var mInsuranceDate: String
    private var mCompositeDisposable_update_Photo: CompositeDisposable? = null
    private lateinit var dataManager: DataManager
    private lateinit var iv_profile: ImageView
    private  var IsprofilePhoto:Boolean?=false
    private lateinit var mFileUserPhoto: File
    var cal = Calendar.getInstance()

    override fun selctok() {
        activity!!.supportFragmentManager.popBackStack()
        AppConstants.ISDONE5 ="done"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View= inflater.inflate(R.layout.fragment_add_vehicle_insurance, container, false)
        view.tv_insurance.text=getString(R.string.add_insurance)
        view.tv_photo_insurance.text=getString(R.string.attach_insurance_photo)
        et_select_company_name=view.et_select_company_name
        et_insurancedate=view.et_insurancedate
        iv_profile=view.iv_profile
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()


        view.relative_profile.setOnClickListener {
            startPic(4, 4)
        }

        // create an OnDateSetListener
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }
        et_insurancedate.setOnClickListener {
            DatePickerDialog(activity!!, R.style.MyDialogTheme,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()


        }
        view.save.setOnClickListener {
            if(!validationForAddDriverAdaharPhoto()){
                uploadingUserPhoto()


            }


        }
        return view
    }

    private fun updateDateInView() {
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat,Locale.US)
        et_insurancedate.setText(sdf.format(cal.getTime()))
    }

    private fun startPic(aspectRatioX: Int, aspectRatioY: Int) {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setActivityTitle("Crop")

            .setCropMenuCropButtonTitle("Done")
            .setCropMenuCropButtonIcon(R.drawable.ic_green_right_rounded)

            .start(context!!, this)
    }

    private fun onImageResult(file: File) {
        IsprofilePhoto=true
        Glide.with(this).load(file).into(iv_profile)
        mFileUserPhoto = file

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
    // api call for user registration
    private fun uploadingUserPhoto() {

        showDialogLoading()
        val file = FileHelper.reduceFileSize(mFileUserPhoto!!)
        val reqFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file!!)
        val body: MultipartBody.Part = MultipartBody.Part.createFormData("photo", file!!.name.toString(), reqFile)
        val adminid: RequestBody = RequestBody.create(MediaType.parse("text/plain"),dataManager.getAdminId()!! )
        val vehicleid: RequestBody = RequestBody.create(MediaType.parse("text/plain"),dataManager.getVehicleId()!!)
        val companyNmae: RequestBody = RequestBody.create(MediaType.parse("text/plain"),mCompanyNmae)
        val insuranceDate: RequestBody = RequestBody.create(MediaType.parse("text/plain"),mInsuranceDate)
        val type: RequestBody = RequestBody.create(MediaType.parse("text/plain"), "true")


        mCompositeDisposable_update_Photo = CompositeDisposable()

        mCompositeDisposable_update_Photo?.add(
            ApiRequestClient.createREtrofitInstance()
                .uploadImageForVehicleInsurance(body, adminid, vehicleid,companyNmae, insuranceDate,type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseUser_Photo, this::handleError_user_photo)
        )
    }


    // handle sucess response of api call registration
    private fun handleResponseUser_Photo(response: ResponseFromServerGeneric) {
        hideDialogLoading()

        if (response.response_code.equals("0")) {
            UtiliyMethods.showDialogwithMessage(activity!!,response.response_message,this )



        }

        mCompositeDisposable_update_Photo?.clear()

    }


    // handle failure response of api call registration
    private fun handleError_user_photo(error: Throwable) {

        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable_update_Photo?.clear()

    }

    private fun validationForAddDriverAdaharPhoto():Boolean {

        mCompanyNmae = et_select_company_name.text.toString()
        mInsuranceDate = et_insurancedate.text.toString()



        if(Validation.isEmptyField(mCompanyNmae)){
            et_select_company_name.error = getString(R.string.error_no_text)
            et_select_company_name.requestFocus()
            return true

        }

        if(Validation.isEmptyField(mInsuranceDate)){
            et_insurancedate.error = getString(R.string.error_no_text)
            et_insurancedate.requestFocus()
            return true

        }

        if(!IsprofilePhoto!!){
            showDialogWithDismiss(getString(R.string.profileImage))
            return true
        }



        return false


    }



}
