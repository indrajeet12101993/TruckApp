package com.truckintransit.operator.activity


import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import com.truckintransit.operator.R
import com.truckintransit.operator.base.BaseActivity
import com.truckintransit.operator.base.BaseApplication
import com.truckintransit.operator.constants.AppConstants.SPLASH_DELAY
import com.truckintransit.operator.dataprefence.DataManager

class SplashActivity : BaseActivity() {
    private var mDelayHandler: Handler? = null
    lateinit var dataManager: DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash)
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()
        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
    }

    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {

            if(dataManager.getLoggedIn()){
                launchActivityWithFinish<DashBoardActvity>()

            }else{
                launchActivityWithFinish<ChooseLanguageActivity>()
            }


        }
    }

    override fun onDestroy() {
        mDelayHandler?.removeCallbacks(mRunnable)
        super.onDestroy()
    }

    override fun onBackPressed() {
        mDelayHandler?.removeCallbacks(mRunnable)
        super.onBackPressed()
    }
}
