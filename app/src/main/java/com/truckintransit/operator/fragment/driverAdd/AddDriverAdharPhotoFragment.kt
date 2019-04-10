package com.truckintransit.operator.fragment.driverAdd


import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.fragment_add_driver_adhar_photo.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class AddDriverAdharPhotoFragment : BaseFragment(), ListnerForDialog {
    override fun selctok() {
        activity!!.supportFragmentManager.popBackStack()
        AppConstants.ISDONE2 ="done"
    }

    private var mCompositeDisposable_update_Photo: CompositeDisposable? = null
    private lateinit var dataManager: DataManager
   // private lateinit var mUserDetails: List<Result>

    private lateinit var iv_profile: ImageView
    private lateinit var iv_profile1: ImageView
    private lateinit var et_adaharnumber: TextInputEditText
    private lateinit var mNumberAdhar: String
    private lateinit var mIsphoto1: String

    private  var IsprofilePhoto:Boolean?=false
    private  var IsprofilePhoto1:Boolean?=false
    private lateinit var mFileUserPhoto: File
    private lateinit var mFileUserPhoto1: File
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_add_driver_adhar_photo, container, false)

        iv_profile=view.iv_profile
        iv_profile1=view.iv_profile1
        et_adaharnumber=view.et_adaharnumber
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()

        view.tv_attach.text=getString(R.string.attach_aaddhar_photo)
        view.relative_profile.setOnClickListener {
            mIsphoto1="1"
            startPic(200, 200)
        }
        view.relative_profile1.setOnClickListener {
            mIsphoto1="2"
            startPic(10, 10)
        }

        view.tv_next.setOnClickListener {
            if(!validationForAddDriverAdaharPhoto()){
                uploadingUserPhoto()


            }


        }
        return view
    }

    // api call for user registration
    private fun uploadingUserPhoto() {

        showDialogLoading()
        val file = FileHelper.reduceFileSize(mFileUserPhoto!!)
        val reqFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file!!)
        val body: MultipartBody.Part = MultipartBody.Part.createFormData("adhaar_photo", file!!.name.toString(), reqFile)
        val file1 = FileHelper.reduceFileSize(mFileUserPhoto1!!)
        val reqFile1: RequestBody = RequestBody.create(MediaType.parse("image/*"), file1!!)
        val body1: MultipartBody.Part = MultipartBody.Part.createFormData("adhaar_backphoto", file1!!.name.toString(),reqFile1)
        val id: RequestBody = RequestBody.create(MediaType.parse("text/plain"),dataManager.getDriverId()!!)
        val adharnumber: RequestBody = RequestBody.create(MediaType.parse("text/plain"),mNumberAdhar)
        val type: RequestBody = RequestBody.create(MediaType.parse("text/plain"), "true")


        mCompositeDisposable_update_Photo = CompositeDisposable()

        mCompositeDisposable_update_Photo?.add(
            ApiRequestClient.createREtrofitInstance()
                .uploadImageAdhar(body,body1, id, adharnumber, type)
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

        mNumberAdhar = et_adaharnumber.text.toString()



        if(Validation.isEmptyField(mNumberAdhar)){
            et_adaharnumber.error = getString(R.string.error_no_text)
            et_adaharnumber.requestFocus()
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
