package com.truckintransit.operator.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.truckintransit.operator.fragment.AllCarsFragment
import com.truckintransit.operator.fragment.AllDriversFragment

class ViewPagerDriverProfileAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {


        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = AllDriversFragment()
            1 -> fragment = AllCarsFragment()


        }

        return fragment!!


    }

    override fun getCount(): Int {
        return 2
    }
    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "All Drivers"
            1 -> "All Cars"

            else -> {
                return ""
            }
        }
    }
}