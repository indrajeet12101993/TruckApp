package com.truckintransit.operator.activity


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.truckintransit.operator.R
import com.truckintransit.operator.base.BaseActivity
import com.truckintransit.operator.base.BaseApplication
import com.truckintransit.operator.constants.AppConstants
import com.truckintransit.operator.dataprefence.DataManager
import com.truckintransit.operator.fragment.basicDetails.BankKycFragment
import com.truckintransit.operator.fragment.basicDetails.BasicDetailsFragment
import com.truckintransit.operator.fragment.basicDetails.TransportFragment

class RegistrationActvity : BaseActivity() {
    lateinit var dataManager: DataManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_actvity)
        initCustomToolbar(getString(R.string.registration))
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()


        val strOpenType: String = intent.getStringExtra(AppConstants.INTENTOPENREGISTRATION)
        if (strOpenType == getString(R.string.registration)) {
            replaceFragment(BasicDetailsFragment())
        }
        if (strOpenType == getString(R.string.transport)) {
            replaceFragment(TransportFragment())
        }
        if (strOpenType == getString(R.string.bank)) {
            replaceFragment(BankKycFragment())
        }

    }

     private fun addBasicDetailsFragment() {

         val basicDetailsFragment = BasicDetailsFragment()
         val transaction = supportFragmentManager.beginTransaction()
         transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
         transaction.add(R.id.place_holder_for_fragment, basicDetailsFragment)
         transaction.commit()

     }

    private fun replaceFragment(fragment: Fragment) {

        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.replace(R.id.place_holder_for_fragment, fragment)
       // transaction.addToBackStack(null)
        transaction.commit()
    }


}
