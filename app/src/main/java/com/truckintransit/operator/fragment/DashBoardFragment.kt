package com.truckintransit.operator.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.truckintransit.operator.R
import com.truckintransit.operator.base.BaseApplication
import com.truckintransit.operator.base.BaseFragment
import com.truckintransit.operator.dataprefence.DataManager
import kotlinx.android.synthetic.main.fragment_dash_board.view.*


class DashBoardFragment : BaseFragment(){
    lateinit var dataManager: DataManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View =inflater.inflate(R.layout.fragment_dash_board, container, false)
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()


        return view
    }


}
