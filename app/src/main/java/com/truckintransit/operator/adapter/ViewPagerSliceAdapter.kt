package com.truckintransit.operator.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.truckintransit.operator.fragment.welcomeScreen.FragmentInroOne
import com.truckintransit.operator.fragment.welcomeScreen.FragmentInteoFour
import com.truckintransit.operator.fragment.welcomeScreen.FragmentIntroThree
import com.truckintransit.operator.fragment.welcomeScreen.FragmentIntroTwo

class ViewPagerSliceAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {


    override fun getItem(position: Int): Fragment {


        var fragment: Fragment? = null

        when (position) {
            0 -> fragment = FragmentIntroTwo()
            1 -> fragment = FragmentIntroThree()
            2 -> fragment = FragmentInteoFour()
            3 -> fragment = FragmentInroOne()


        }

        return fragment!!


    }

    override fun getCount(): Int {
        return 4
    }


}