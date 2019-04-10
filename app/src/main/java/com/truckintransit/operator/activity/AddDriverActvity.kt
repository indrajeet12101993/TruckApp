package com.truckintransit.operator.activity


import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.truckintransit.operator.R
import com.truckintransit.operator.base.BaseActivity
import com.truckintransit.operator.constants.AppConstants
import com.truckintransit.operator.fragment.driverEdit.AddDriverBasicInfoEditFrafment
import com.truckintransit.operator.fragment.driverAdd.AddDriverBasicInfoFragment
import kotlinx.android.synthetic.main.app_bar_dash_board_actvity.*

class AddDriverActvity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_driver_actvity)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if(intent.getStringExtra(AppConstants.ISMENUENABLEDEDIT)=="true"){
            toolbar_title.text=getString(R.string.edit_driver)
            replaceFragment(AddDriverBasicInfoEditFrafment())

        }else{
            toolbar_title.text=getString(R.string.add_driver)
            replaceFragment(AddDriverBasicInfoFragment())
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    private fun replaceFragment(fragment: Fragment) {

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.place_holder_for_fragment, fragment)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit()
    }
}
