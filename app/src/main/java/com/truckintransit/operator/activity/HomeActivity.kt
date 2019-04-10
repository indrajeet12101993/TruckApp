package com.truckintransit.operator.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.truckintransit.operator.R
import com.truckintransit.operator.base.BaseApplication
import com.truckintransit.operator.dataprefence.DataManager

class HomeActivity : AppCompatActivity() {
    lateinit var dataManager: DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()
        dataManager.setLoggedIn(true)
    }
}
