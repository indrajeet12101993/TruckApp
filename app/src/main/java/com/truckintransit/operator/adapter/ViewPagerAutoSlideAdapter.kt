package com.truckintransit.operator.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.truckintransit.operator.R
import kotlinx.android.synthetic.main.custom_layout_viewpager_for_auto_layout.view.*
import java.util.ArrayList

class ViewPagerAutoSlideAdapter(val mContext: Context, val mList: ArrayList<Int>) : PagerAdapter() {


    private var layoutInflater: LayoutInflater? = null


    override fun getCount(): Int {
        return mList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        layoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater!!.inflate(R.layout.custom_layout_viewpager_for_auto_layout, null)
        Glide.with(mContext).load(mList[position]).into(view.iv_auto_slide)
        val vp = container as ViewPager
        vp.addView(view, 0)
        return view

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)

    }
}