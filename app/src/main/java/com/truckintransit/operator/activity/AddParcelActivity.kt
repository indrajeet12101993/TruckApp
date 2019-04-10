package com.truckintransit.operator.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.truckintransit.operator.R
import com.truckintransit.operator.base.BaseActivity

class AddParcelActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_parcel)
    }
}
