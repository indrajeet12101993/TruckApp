package com.truckintransit.operator.activity


import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.truckintransit.operator.R
import com.truckintransit.operator.adapter.ViewPagerSliceAdapter
import com.truckintransit.operator.base.BaseActivity
import com.truckintransit.operator.base.BaseApplication
import com.truckintransit.operator.dataprefence.DataManager
import kotlinx.android.synthetic.main.activity_welcome.*
import java.util.*


class WelcomeActivity : BaseActivity() {
    private var dotscount: Int = 0
    private lateinit var dots: Array<ImageView?>
    private var currentPage = 0
    private var NUM_PAGES = 0
    lateinit var dataManager: DataManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val viewPagerAdapter = ViewPagerSliceAdapter(supportFragmentManager)
        view_pager.adapter = viewPagerAdapter
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()
        dotscount = 4
        NUM_PAGES = 4
        dots = arrayOfNulls<ImageView>(dotscount)
        for (j in 0 until dotscount) {

            dots[j] = ImageView(this)
            dots[j]!!.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.non_active_dot))
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            linear_slider_dots.addView(dots[j], params)
        }

        dots[0]!!.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.active_dot))

        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {

                for (i in 0 until dotscount) {
                    dots[i]!!.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.non_active_dot))
                }

                dots[position]!!.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.active_dot))

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        // Auto start of viewpager
        val handler = Handler()

        val Update = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            view_pager!!.setCurrentItem(currentPage++, true)
        }

        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 5000, 5000)

        tv_partner.setOnClickListener {
            launchActivityWithFinish<UserEnterOtpActivity>()

        }


    }


}
