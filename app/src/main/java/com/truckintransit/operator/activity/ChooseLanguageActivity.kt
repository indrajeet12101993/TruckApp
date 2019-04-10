package com.truckintransit.operator.activity


import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.truckintransit.operator.R
import com.truckintransit.operator.base.BaseActivity
import com.truckintransit.operator.base.BaseApplication
import com.truckintransit.operator.dataprefence.DataManager
import kotlinx.android.synthetic.main.activity_choose_language.*


class ChooseLanguageActivity : BaseActivity() {

    lateinit var dataManager: DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_choose_language)
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()


        tv_english.setOnClickListener {

            setNewLocale("en", true)


        }
        tv_hindi.setOnClickListener {

            setNewLocale("hi", true)


        }
    }

    private fun setNewLocale(language: String, restartProcess: Boolean): Boolean {

        BaseApplication.baseApplicationInstance.localeManager.setNewLocale(this, language)

        val i = Intent(this, WelcomeActivity::class.java)
        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))



        if (restartProcess) {
            System.exit(0)
        } else {
            Toast.makeText(this, "Activity restarted", Toast.LENGTH_SHORT).show()
        }


        return true
    }


}
