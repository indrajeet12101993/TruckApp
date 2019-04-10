package com.truckintransit.operator.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.truckintransit.operator.R
import com.truckintransit.operator.base.BaseActivity
import com.truckintransit.operator.base.BaseApplication
import kotlinx.android.synthetic.main.activity_choose_language1.*
import kotlinx.android.synthetic.main.activity_seeting.*

class SeetingActivity : BaseActivity() {
    private lateinit var dialog_custom: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seeting)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.title = getString(R.string.setting)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.yellow))

        tv_add_language.setOnClickListener {
            showDialogForAddLanguage()
        }
        tv_add_kryboard.setOnClickListener {
            val imeManager = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imeManager.showInputMethodPicker()
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
    private fun showDialogForAddLanguage() {
        dialog_custom = Dialog(this)
        dialog_custom.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog_custom.setContentView(R.layout.activity_choose_language1)
        dialog_custom.setCanceledOnTouchOutside(false)
        dialog_custom.setCancelable(true)

        // 1-- english
        //2 -- hindi
        dialog_custom. tv_english.setOnClickListener {
            dialog_custom.dismiss()

            setNewLocale("en", true)


        }
        dialog_custom. tv_hindi.setOnClickListener {
            dialog_custom.dismiss()
            setNewLocale("hi", true)


        }




        dialog_custom.show()

    }

    private fun setNewLocale(language: String, restartProcess: Boolean): Boolean {
        BaseApplication.baseApplicationInstance.localeManager.setNewLocale(this, language)


        val i = Intent(this, DashBoardActvity::class.java)
        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))

        if (restartProcess) {
            System.exit(0)
        } else {
            Toast.makeText(this, "Activity restarted", Toast.LENGTH_SHORT).show()
        }
        return true
    }
}
