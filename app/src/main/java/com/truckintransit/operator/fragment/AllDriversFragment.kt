package com.truckintransit.operator.fragment


import android.app.Activity
import android.app.ActivityOptions
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.truckintransit.operator.R
import com.truckintransit.operator.adapter.AlldriversRecyclerAdapter
import com.truckintransit.operator.callbackInterface.ListnerForNaviagtionItem
import kotlinx.android.synthetic.main.fragment_all_drivers.view.*
import com.truckintransit.operator.activity.AddDriverActvity
import com.truckintransit.operator.activity.AddParcelActivity
import com.truckintransit.operator.base.BaseApplication
import com.truckintransit.operator.base.BaseFragment
import com.truckintransit.operator.constants.AppConstants
import com.truckintransit.operator.constants.AppConstants.DRIVERID
import com.truckintransit.operator.dataprefence.DataManager
import com.truckintransit.operator.newtworking.ApiRequestClient

import com.truckintransit.operator.pojo.driverlist.DriverDetail
import com.truckintransit.operator.pojo.driverlist.ResponseFromServerDriverList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.select_vehicle_type.*


class AllDriversFragment : BaseFragment(), ListnerForNaviagtionItem {
    private var mCompositeDisposable: CompositeDisposable? = null
    private lateinit var mList: ArrayList<DriverDetail>
    private lateinit var mAdapter: AlldriversRecyclerAdapter
    private lateinit var dataManager: DataManager
    private var isapiRefresh: Boolean = false
    private lateinit var dialog_custom: Dialog
    override fun itemSelcectPosition(position: Int) {

        val driverDetail = mList[position]
        DRIVERID = driverDetail.id


        // true when user comes from driver list
        launchActivityForAddDriver(AddDriverActvity(), "true")

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_all_drivers, container, false)
        //list of recyclerview
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity!!)
        view.rv_all_drivers.layoutManager = layoutManager
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()
        mList = ArrayList<DriverDetail>()
        mAdapter = AlldriversRecyclerAdapter(activity!!, mList, this)
        view.rv_all_drivers.adapter = mAdapter

        view.rv_all_drivers.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && view.floating_action_button.getVisibility() == View.VISIBLE) {
                    view.floating_action_button.hide()
                } else if (dy < 0 && view.floating_action_button.getVisibility() != View.VISIBLE) {
                    view.floating_action_button.show()
                }
            }
        })

        view.floating_action_button.setOnClickListener {
            isapiRefresh = true

            launchActivityForAddDriver(AddDriverActvity(), "False")





        }





        startLoadingForDriverList()

        return view
    }


    override fun onResume() {
        super.onResume()
        if (isapiRefresh) {
            startLoadingForDriverList()

        }

    }

    //geeting  total driver list
    private fun startLoadingForDriverList() {
        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServerListOfDriver(dataManager.getAdminId()!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        )
    }

    // handle sucess response of api call of get phone number
    private fun handleResponse(response: ResponseFromServerDriverList) {
        hideDialogLoading()

        if (response.driver_detail.isNotEmpty()) {
            mList.clear()
            mList.addAll(response.driver_detail)
            mAdapter.notifyDataSetChanged()
        } else {
            showSnackBar(getString(R.string.nodriverfound))
        }

        mCompositeDisposable?.clear()


    }


    // handle failure response of api call of get phone number
    private fun handleError(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }

    private fun launchActivityForAddDriver(T: Activity, data: String) {

        val intent = Intent(activity, T::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Apply activity transition
            intent.putExtra(AppConstants.ISMENUENABLEDEDIT, data)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity!!).toBundle())
        } else {
            // Swap without transition
            startActivity(intent)
        }


    }


}
