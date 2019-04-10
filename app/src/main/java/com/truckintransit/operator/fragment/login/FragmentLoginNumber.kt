package com.truckintransit.operator.fragment.login



import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.truckintransit.operator.R
import com.truckintransit.operator.base.BaseFragment
import com.truckintransit.operator.utils.Validation
import com.truckintransit.operator.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_fragment_login_number.*
import kotlinx.android.synthetic.main.fragment_fragment_login_number.view.*


class FragmentLoginNumber : BaseFragment() {
    private var mSharedViewModel: SharedViewModel? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSharedViewModel = activity?.run {
            ViewModelProviders .of(this).get(SharedViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_fragment_login_number, container, false)



        view.tv_next.setOnClickListener {
            val phone_number: String = et_phone_number.text.toString()

            if (Validation.isEmptyField(phone_number)) {
                et_phone_number.error = getString(R.string.emptynumber)

                return@setOnClickListener


            }
            if (Validation.isValidPhoneNumber(phone_number)) {
                et_phone_number.error = getString(R.string.valid_input_number)
                return@setOnClickListener

            }
              hideKeyboard(et_phone_number)

            if (isNetworkAvailable()) {
                mSharedViewModel!!.inputNumber.postValue(phone_number)
                replaceFragment(FragmentOtp())

            } else {
                showSnackBar(getString(R.string.no_internet))
            }




        }

        return view
    }

    private fun replaceFragment(fragment: Fragment) {

        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.replace(R.id.place_holder_for_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}
