package com.truckintransit.operator.fragment.driverAdd


import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.fragment_add_driver_dl_photo.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class AddDriverDlPhotoFragment : BaseFragment(), ListnerForDialog {
    override fun selctok() {
        activity!!.supportFragmentManager.popBackStack()
        AppConstants.ISDONE3 ="done"
    }

    private var mCompositeDisposable_update_Photo: CompositeDisposable? = null
    private var mCompositeDisposable: CompositeDisposable? = null
    private lateinit var dataManager: DataManager

    private lateinit var iv_profile: ImageView
    private lateinit var iv_profile1: ImageView
    private lateinit var et_dlnumber: TextInputEditText
    private lateinit var et_date: TextInputEditText
    private lateinit var mIsphoto1: String
    private lateinit var mNumberdl: String
    private lateinit var mNumberdate: String

    private  var IsprofilePhoto:Boolean?=false
    private  var IsprofilePhoto1:Boolean?=false
    private lateinit var mFileUserPhoto: File
    private lateinit var mFileUserPhoto1: File
    var cal = Calendar.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View= inflater.inflate(R.layout.fragment_add_driver_dl_photo, container, false)
        iv_profile=view.iv_profile
        iv_profile1=view.iv_profile1
        et_dlnumber=view.et_dlnumber
        et_date=view.et_date
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()

        view.tv_attach.text=getString(R.string.attach_dl_photo)
        view.relative_profile.setOnClickListener {
            mIsphoto1="1"
            startPic(4, 4)
        }
        view.relative_profile1.setOnClickListener {
            mIsphoto1="2"
            startPic(4, 4)
        }
        view.tv_next.setOnClickListener {
            if(!validationForAddDriverAdaharPhoto()){

                uploadingUserPhoto()

            }


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
        et_date.setOnClickListener {
            DatePickerDialog(activity!!, R.style.MyDialogTheme,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()


        }


        return view
    }

    private fun updateDateInView() {
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat,Locale.US)
        et_date.setText(sdf.format(cal.getTime()))
    }

    // api call for user registration
    private fun uploadingUserPhoto() {

        showDialogLoading()
        val file = FileHelper.reduceFileSize(mFileUserPhoto!!)
        val reqFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file!!)
        val body: MultipartBody.Part = MultipartBody.Part.createFormData("dl_photo", file!!.name.toString(), reqFile)
        val file1 = FileHelper.reduceFileSize(mFileUserPhoto1!!)
        val reqFile1: RequestBody = RequestBody.create(MediaType.parse("image/*"), file1!!)
        val body1: MultipartBody.Part = MultipartBody.Part.createFormData("dl_backphoto", file1!!.name.toString(),reqFile1)
        val id: RequestBody = RequestBody.create(MediaType.parse("text/plain"),dataManager.getDriverId()!!)

        val dldate: RequestBody = RequestBody.create(MediaType.parse("text/plain"),mNumberdate )
        val dlnumber: RequestBody = RequestBody.create(MediaType.parse("text/plain"),mNumberdl)
        val type: RequestBody = RequestBody.create(MediaType.parse("text/plain"), "true")


        mCompositeDisposable_update_Photo = CompositeDisposable()

        mCompositeDisposable_update_Photo?.add(
            ApiRequestClient.createREtrofitInstance()
                .uploadImageDl(body,body1, id,dldate, dlnumber, type)
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
        showSnackBar(error.message.toString())
        mCompositeDisposable_update_Photo?.clear()

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
        if(mIsphoto1=="1"){
            IsprofilePhoto=true
            Glide.with(this).load(file).into(iv_profile)
            mFileUserPhoto = file
        }

        if(mIsphoto1=="2"){
            IsprofilePhoto1=true
            Glide.with(this).load(file).into(iv_profile1)
            mFileUserPhoto1= file
        }


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

    private fun validationForAddDriverAdaharPhoto():Boolean {

        mNumberdl = et_dlnumber.text.toString()
        mNumberdate = et_date.text.toString()



        if(Validation.isEmptyField(mNumberdl)){
            et_dlnumber.error = getString(R.string.error_no_text)
            et_dlnumber.requestFocus()
            return true

        }
        if(Validation.isEmptyField(mNumberdate)){
            et_date.error = getString(R.string.error_no_text)
            et_date.requestFocus()
            return true

        }

        if(!IsprofilePhoto!!){
            showDialogWithDismiss(getString(R.string.profileImage))
            return true
        }

        if(!IsprofilePhoto1!!){
            showDialogWithDismiss(getString(R.string.profileImage))
            return true
        }

        return false


    }

}
