package com.truckintransit.operator.activity

import android.content.BroadcastReceiver
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.truckintransit.operator.R
import com.truckintransit.operator.adapter.NaviagtionDrawerRecyclerAdapter
import com.truckintransit.operator.base.BaseActivity
import com.truckintransit.operator.callbackInterface.ListnerForNaviagtionItem
import com.truckintransit.operator.fragment.DashBoardFragment
import kotlinx.android.synthetic.main.activity_dash_board_actvity.*
import kotlinx.android.synthetic.main.app_bar_dash_board_actvity.*
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.truckintransit.operator.base.BaseApplication
import com.truckintransit.operator.callbackInterface.InterfaceResponseAdmin
import com.truckintransit.operator.constants.AppConstants.FindRiderId
import com.truckintransit.operator.dataprefence.DataManager
import com.truckintransit.operator.fragment.location.DriverLocationFragment
import com.truckintransit.operator.fragment.UserBlockedFragment
import com.truckintransit.operator.newtworking.ApiRequestClient
import com.truckintransit.operator.pojo.adminDash.ResponseFromServerAdminDashBoard
import com.truckintransit.operator.pojo.driverDash.ResponseFromServerDriverDshBoard
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.nav_header_dash_board_actvity.*


class DashBoardActvity : BaseActivity(), ListnerForNaviagtionItem {
    private lateinit var mList: ArrayList<String>
    private lateinit var dataManager: DataManager
    private var mCompositeDisposable: CompositeDisposable? = null
    private var mAdapter: NaviagtionDrawerRecyclerAdapter? = null
    private var mRegistrationBroadcastReceiver: BroadcastReceiver? = null
    private lateinit var mInterfaceAdmin: InterfaceResponseAdmin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board_actvity)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()
        dataManager.setLoggedIn(true)
        mList = ArrayList<String>()
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        //list of recyclerview_menu
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager
        rv_menu_items.layoutManager = layoutManager
        mAdapter = NaviagtionDrawerRecyclerAdapter(this, mList, this)
        rv_menu_items.adapter = mAdapter


        if (dataManager.getDriverType() == "1") {
            startLoadingAdminDashBoard(object : InterfaceResponseAdmin {
                override fun resultAdmin(response: ResponseFromServerAdminDashBoard) {
                    tv_username.text = response.Basic_info[0].name
                    tv_number.text = response.Basic_info[0].mobile
                    if (!response.Basic_info[0].image.isEmpty())
                        Glide.with(this@DashBoardActvity).load(response.Basic_info[0].image).into(imageView)
                    if (!response.Basic_info[0].is_blocked.isEmpty()) {
                        dataManager.setIsBlocked(response.Basic_info[0].is_blocked)


                    }
                    // 1-- blocked
                    // 2-- no blocked
                    if (response.Basic_info[0].is_blocked == "1") {
                        card_isapprove.visibility = View.VISIBLE
                        tv_message.text = getString(R.string.you_profile_is_been_not_approved_please_wait_for_approval)
                        tv_message1.text = getString(R.string.inactvie)
                        replaceFragment(UserBlockedFragment(), getString(R.string.accountnotapproved))
                        naviagtionshowAdminUnApproved()

                    } else {
                        card_isapprove.visibility = View.VISIBLE
                        tv_message.text = getString(R.string.active_message)
                        tv_message1.text = getString(R.string.active)
                        naviagtionshowAdminApproved()
                    }
                }
            }

            )


        } else if (dataManager.getDriverType() == "2") {
            startLoadingDriverDashBoard(dataManager.getDriverOwnId()!!)

        } else if (dataManager.getDriverType() == "3") {
            startLoadingAdminDashBoard(object : InterfaceResponseAdmin {
                override fun resultAdmin(response: ResponseFromServerAdminDashBoard) {
                    tv_username.text = response.Basic_info[0].name
                    tv_number.text = response.Basic_info[0].mobile
                    if (!response.Basic_info[0].image.isEmpty())
                        Glide.with(this@DashBoardActvity).load(response.Basic_info[0].image).into(imageView)
                    if (!response.Basic_info[0].is_blocked.isEmpty()) {
                        dataManager.setIsBlocked(response.Basic_info[0].is_blocked)


                    }
                    // 1-- blocked
                    // 2-- no blocked
                    if (response.Basic_info[0].is_blocked == "1") {
                        card_isapprove.visibility = View.VISIBLE
                        tv_message.text = getString(R.string.you_profile_is_been_not_approved_please_wait_for_approval)
                        tv_message1.text = getString(R.string.inactvie)
                        replaceFragment(UserBlockedFragment(), getString(R.string.accountnotapproved))
                        naviagtionshowAdminUnApproved()

                    } else {
                        card_isapprove.visibility = View.GONE
                        tv_message.text = getString(R.string.active_message_driver_admin)
                        tv_message1.text = getString(R.string.active)
                        FindRiderId = dataManager.getDriverOwnId()!!
                        naviagtionshowAdminDriverApproved()
                    }
                }
            })

        }


    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun itemSelcectPosition(position: Int) {

        val navItemType = mList.get(position)


        if (navItemType == getString(R.string.findrider)) {
            replaceFragment(DriverLocationFragment(), getString(R.string.findrider))

        }
        if (navItemType == getString(R.string.dashboarad)) {

            replaceFragment(DashBoardFragment(), mList.get(position))
        }
        if (navItemType == getString(R.string.rides)) {

           // launchActivity<CustomerCallActivity>()
        }
        if (navItemType == getString(R.string.yourprofile)) {
            launchActivity<YourProfileActivity>()
        }

        if (navItemType == getString(R.string.yourdriverprofile)) {
            launchActivity<YourDriverProfileActivity>()
        }
        if (navItemType == getString(R.string.yourvehile)) {
            launchActivity<AddVehicleActivity>()
        }
        if (navItemType == getString(R.string.yourearnings)) {
            launchActivity<YourTotalEarningsActivity>()

        }
        if (navItemType == getString(R.string.refer)) {

            launchActivity<ReferAndEarnActivity>()


        }
        if (navItemType == getString(R.string.support)) {

            launchActivity<SupportActivity>()


        }
        if (navItemType == getString(R.string.logout)) {


            dataManager.setLoggedIn(false)
            endActivityWithClearTask<UserEnterOtpActivity>()


        }
        drawer_layout.closeDrawer(GravityCompat.START)
    }


    private fun replaceFragment(fragment: Fragment, title: String) {
        toolbar_title.text = title
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.place_holder_for_fragment, fragment)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit()
    }

    //geeting  number for admin DashBoard
    private fun startLoadingAdminDashBoard(interfaceadamin: InterfaceResponseAdmin) {

        mInterfaceAdmin = interfaceadamin
        showDialogLoading()

        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServerAdmin(dataManager.getAdminId()!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_Admin, this::handleError_Admin)
        )
    }

    // handle sucess response of api call of get phone number
    private fun handleResponse_Admin(response: ResponseFromServerAdminDashBoard) {
        hideDialogLoading()
        mCompositeDisposable?.clear()
        mInterfaceAdmin.resultAdmin(response)


    }


    // handle failure response of api call of get phone number
    private fun handleError_Admin(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }


    //geeting  number for driver DashBoard
    private fun startLoadingDriverDashBoard(id: String) {


        showDialogLoading()

        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServerDriverDashBoard(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_Driver, this::handleError_Driver)
        )
    }

    // handle sucess response of api call
    private fun handleResponse_Driver(response: ResponseFromServerDriverDshBoard) {
        hideDialogLoading()
        mCompositeDisposable?.clear()
        tv_username.text = response.Basic_info[0].name
        tv_number.text = response.Basic_info[0].mobile_no

        if (!response.Basic_info[0].driver_approve.isEmpty()) {
            dataManager.setIsBlocked(response.Basic_info[0].driver_approve)
        }
        if (!response.Basic_info[0].driver_pic.isEmpty())
            Glide.with(this).load(response.Basic_info[0].driver_pic).into(imageView)
        if (response.Basic_info[0].driver_approve == "1") {
            card_isapprove.visibility = View.VISIBLE
            tv_message.text = getString(R.string.you_profile_is_been_not_approved_please_wait_for_approval)
            tv_message1.text = getString(R.string.inactvie)
            replaceFragment(UserBlockedFragment(), getString(R.string.accountnotapproved))
            showSnackBar(getString(R.string.you_profile_is_been_not_approved_please_wait_for_approval))
            naviagtionshowAdminUnApproved()


        } else {
            card_isapprove.visibility = View.GONE
            FindRiderId = dataManager.getDriverOwnId()!!
            naviagtionshowDriverApproved()
        }

    }

    private fun naviagtionshowDriverApproved() {

        replaceFragment(DriverLocationFragment(), getString(R.string.findrider))
        mList.add(getString(R.string.findrider))
        mList.add(getString(R.string.dashboarad))
        mList.add(getString(R.string.rides))
        mList.add(getString(R.string.yourprofile))
        mList.add(getString(R.string.yourearnings))
        mList.add(getString(R.string.payments))
        mList.add(getString(R.string.refer))
        mList.add(getString(R.string.support))
        mList.add(getString(R.string.notifications))
        mList.add(getString(R.string.about))
        mList.add(getString(R.string.privacy))
        mList.add(getString(R.string.terms))
        mList.add(getString(R.string.logout))

        mAdapter!!.notifyDataSetChanged()

    }

    private fun naviagtionshowAdminUnApproved() {
        mList.add(getString(R.string.yourdriverprofile))
        mList.add(getString(R.string.yourvehile))
        mList.add(getString(R.string.logout))
        mAdapter!!.notifyDataSetChanged()
    }

    private fun naviagtionshowAdminApproved() {

        mList.add(getString(R.string.yourprofile))
        mList.add(getString(R.string.yourdriverprofile))
        mList.add(getString(R.string.yourvehile))
        mList.add(getString(R.string.refer))
        mList.add(getString(R.string.support))
        mList.add(getString(R.string.notifications))
        mList.add(getString(R.string.about))
        mList.add(getString(R.string.privacy))
        mList.add(getString(R.string.terms))
        mList.add(getString(R.string.logout))
        mAdapter!!.notifyDataSetChanged()

    }

    private fun naviagtionshowAdminDriverApproved() {
        mList.add(getString(R.string.yourprofile))
        mList.add(getString(R.string.yourdriverprofile))
        mList.add(getString(R.string.yourvehile))
        mList.add(getString(R.string.findrider))
        mList.add(getString(R.string.rides))
        mList.add(getString(R.string.dashboarad))
        mList.add(getString(R.string.refer))
        mList.add(getString(R.string.payments))
        mList.add(getString(R.string.support))
        mList.add(getString(R.string.notifications))
        mList.add(getString(R.string.about))
        mList.add(getString(R.string.privacy))
        mList.add(getString(R.string.terms))
        mList.add(getString(R.string.logout))


        mAdapter!!.notifyDataSetChanged()
        replaceFragment(DriverLocationFragment(), getString(R.string.findrider))

    }


    // handle failure response of api call of get phone number
    private fun handleError_Driver(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }
}




