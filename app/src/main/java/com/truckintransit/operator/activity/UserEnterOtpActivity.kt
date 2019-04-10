package com.truckintransit.operator.activity


import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.truckintransit.operator.R
import com.truckintransit.operator.base.BaseActivity
import com.truckintransit.operator.base.BaseApplication
import com.truckintransit.operator.dataprefence.DataManager
import com.truckintransit.operator.fragment.login.FragmentLoginNumber

class UserEnterOtpActivity : BaseActivity() {
    lateinit var dataManager: DataManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_enter_otp)
        addNumberFragment()
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()

    }

    private fun addNumberFragment() {

        val homeFragment = FragmentLoginNumber()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.place_holder_for_fragment, homeFragment)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.commit()
    }
}
