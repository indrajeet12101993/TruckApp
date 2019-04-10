package com.truckintransit.operator.fragment.basicDetails

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.truckintransit.operator.R
import kotlinx.android.synthetic.main.fragment_basic_details.view.*
import android.content.Intent
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.truckintransit.operator.base.BaseApplication
import com.truckintransit.operator.base.BaseFragment
import com.truckintransit.operator.callbackInterface.ListnerForDialog
import com.truckintransit.operator.dataprefence.DataManager
import com.truckintransit.operator.newtworking.ApiRequestClient
import com.truckintransit.operator.pojo.ResponseFromServerGeneric
import com.truckintransit.operator.utils.FileHelper
import com.truckintransit.operator.utils.FileHelper.reduceFileSize
import com.truckintransit.operator.utils.UtiliyMethods
import com.truckintransit.operator.utils.Validation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File



class BasicDetailsFragment : BaseFragment(), ListnerForDialog {

    private var mCompositeDisposable_update_Photo: CompositeDisposable? = null
    private lateinit var mFileUserPhoto: File
    private lateinit var iv_profile: ImageView
    private lateinit var et_name: TextInputEditText
    private lateinit var et_mail_id: TextInputEditText
    private lateinit var et_password: TextInputEditText
    private lateinit var et_confirm_password: TextInputEditText

    private lateinit var mName: String
    private lateinit var mMailId: String
    private lateinit var mPassword: String
    private lateinit var mComfirmPassword: String
    private  var IsprofilePhoto:Boolean?=false

    private lateinit var dataManager: DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_basic_details, container, false)

        iv_profile = view.iv_profile
        et_name = view.et_name
        et_mail_id = view.et_mail_id
        et_password = view.et_password
        et_confirm_password = view.et_confirm_password
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()


        view.relative_profile.setOnClickListener {
            startPic(4, 4)
        }
        view.tv_next.setOnClickListener {


            if(!validationForUserRegistration()){
                uploadingUserPhoto()
            }




        }
        return view
    }

    private fun validationForUserRegistration():Boolean {

        mName = et_name.text.toString()
        mMailId = et_mail_id.text.toString()
        mPassword = et_password.text.toString()
        mComfirmPassword = et_confirm_password.text.toString()

        if(Validation.isEmptyField(mName)){
            et_name.error = getString(R.string.error_no_text)
            et_name.requestFocus()
            return true

        }
        if(Validation.isEmptyField(mMailId)){
            et_mail_id.error = getString(R.string.error_no_text)
            et_mail_id.requestFocus()
            return true

        }
        if(Validation.isEmptyField(mPassword)){
            et_password.error = getString(R.string.error_no_text)
            et_password.requestFocus()
            return true

        }
        if(Validation.isEmptyField(mComfirmPassword)){
            et_confirm_password.error = getString(R.string.error_no_text)
            et_confirm_password.requestFocus()
            return true

        }
        if(mPassword!=mComfirmPassword){
            et_confirm_password.error = getString(R.string.passwordnotmatch)
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

        showDialogLoading()
        val file = reduceFileSize(mFileUserPhoto!!)
        val reqFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file!!)
        val body: MultipartBody.Part = MultipartBody.Part.createFormData("file", file!!.name.toString(), reqFile)
        val id: RequestBody = RequestBody.create(MediaType.parse("text/plain"),dataManager.getAdminId()!!)
        val name: RequestBody = RequestBody.create(MediaType.parse("text/plain"), mName)
        val email: RequestBody = RequestBody.create(MediaType.parse("text/plain"), mMailId)
        val pass: RequestBody = RequestBody.create(MediaType.parse("text/plain"), mPassword)
        val type: RequestBody = RequestBody.create(MediaType.parse("text/plain"), "true")


        mCompositeDisposable_update_Photo = CompositeDisposable()

        mCompositeDisposable_update_Photo?.add(
            ApiRequestClient.createREtrofitInstance()
                .uploadImage(body, id, name, email, pass, type)
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


    private fun replaceFragment(fragment: Fragment) {

        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.place_holder_for_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()

    }

    override fun selctok() {
        replaceFragment(TransportFragment())
    }


}
