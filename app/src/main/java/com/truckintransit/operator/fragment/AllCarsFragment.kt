package com.truckintransit.operator.fragment


import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.truckintransit.operator.R
import com.truckintransit.operator.activity.*
import com.truckintransit.operator.adapter.AllVehiclesCarAdapter
import com.truckintransit.operator.adapter.CustomadapterDriverList
import com.truckintransit.operator.base.BaseApplication
import com.truckintransit.operator.base.BaseFragment
import com.truckintransit.operator.callbackInterface.InterfaceAssignDriver
import com.truckintransit.operator.callbackInterface.InterfaceDriverSelect
import com.truckintransit.operator.callbackInterface.ListnerForNaviagtionItem
import com.truckintransit.operator.constants.AppConstants
import com.truckintransit.operator.dataprefence.DataManager
import com.truckintransit.operator.newtworking.ApiRequestClient
import com.truckintransit.operator.pojo.ResponseFromServerGeneric
import com.truckintransit.operator.pojo.driverlist.DriverDetail
import com.truckintransit.operator.pojo.driverlist.ResponseFromServerDriverList
import com.truckintransit.operator.pojo.vehicleList.ResponseFromServerVehicleList
import com.truckintransit.operator.pojo.vehicleList.VehicleDetail
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.city_list.*
import kotlinx.android.synthetic.main.fragment_all_cars.view.*
import kotlinx.android.synthetic.main.select_vehicle_type.*


class AllCarsFragment : BaseFragment(), ListnerForNaviagtionItem, InterfaceAssignDriver, InterfaceDriverSelect {
    override fun driverselect(position: Int) {
        val driverDetail = mListVehicle[position]
        mDriverId = driverDetail.id
        assignDriver()

    }


    override fun selectVehicle(position: Int) {
        val vehicleDetail = mList[position]
        mVehicleId = vehicleDetail.id
        dialog_custom1.setTitle(getString(R.string.assign_vehicle))
        startLoadingForDriverList()

    }

    override fun itemSelcectPosition(position: Int) {
        isapiRefresh = true
        val vehicleDetail = mList[position]
        AppConstants.VEHICLEDETAILID = vehicleDetail.id
        // true when user comes from vehicle list
        launchActivity(EditVehicleActivity())
    }

    private var mCompositeDisposable: CompositeDisposable? = null
    private lateinit var mList: ArrayList<VehicleDetail>
    private lateinit var mListVehicle: ArrayList<DriverDetail>
    private lateinit var mAdapter: AllVehiclesCarAdapter
    private lateinit var dataManager: DataManager
    private var isapiRefresh: Boolean = false
    private lateinit var dialog_custom: Dialog
    private lateinit var dialog_custom1: Dialog
    private var mVehicleId: String = ""
    private var mDriverId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_all_cars, container, false)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity!!)
        view.rv_all_drivers.layoutManager = layoutManager
        mList = ArrayList<VehicleDetail>()
        mListVehicle = ArrayList<DriverDetail>()
        dataManager = BaseApplication.baseApplicationInstance.getdatamanger()

        // set adapter to recycleview
        mAdapter = AllVehiclesCarAdapter(activity!!, mList, this, this)
        view.rv_all_drivers.adapter = mAdapter

        view.rv_all_drivers.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && view.floating_action_button.visibility == View.VISIBLE) {
                    view.floating_action_button.hide()
                } else if (dy < 0 && view.floating_action_button.visibility != View.VISIBLE) {
                    view.floating_action_button.show()
                }
            }
        })

        view.floating_action_button.setOnClickListener {
            isapiRefresh = true
            showDialogForVehicleType()


        }
        initCustomDialog()
        startLoadingForVehicleList()
        return view
    }

    private fun showDialogForVehicleType() {
        dialog_custom = Dialog(activity!!)
        dialog_custom.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog_custom.setContentView(R.layout.select_vehicle_type)
        dialog_custom.setCanceledOnTouchOutside(false)
        dialog_custom.setCancelable(true)


        dialog_custom.tv_parcel.setOnClickListener {
            dialog_custom.dismiss()
            launchActivity(AddParcelActivity())
        }
        dialog_custom.tv_truck_hiring.setOnClickListener {
            dialog_custom.dismiss()
            launchActivity(AddVehicalDetailsActivity())
        }

        dialog_custom.show()

    }


    override fun onResume() {
        super.onResume()
        if (isapiRefresh) {
            startLoadingForVehicleList()

        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.place_holder_for_fragment, fragment)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    // initiliaze custom dialog
    private fun initCustomDialog() {
        dialog_custom1 = Dialog(activity!!)
        dialog_custom1.setContentView(R.layout.city_list)
        dialog_custom1.setCanceledOnTouchOutside(false)
        dialog_custom1.setCancelable(true)
        dialog_custom1.recycler_view.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        val itemDecor = DividerItemDecoration(activity, LinearLayout.HORIZONTAL)
        dialog_custom1.recycler_view.addItemDecoration(itemDecor)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            android.R.id.home -> {

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    //geeting  total driver list
    private fun startLoadingForVehicleList() {


        showDialogLoading()


        mCompositeDisposable = CompositeDisposable()

        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServerListOfVehicle(dataManager.getAdminId()!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        )
    }

    // handle sucess response of api call of get phone number
    private fun handleResponse(response: ResponseFromServerVehicleList) {
        hideDialogLoading()
        mCompositeDisposable?.clear()
        if (response.vehicle_detail.isNotEmpty()) {
            mList.clear()
            mList.addAll(response.vehicle_detail)
            mAdapter.notifyDataSetChanged()
        } else {
            showSnackBar(getString(R.string.novehiclefound))
        }


    }


    // handle failure response of api call of get phone number
    private fun handleError(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }


    //geeting  total driver list
    private fun startLoadingForDriverList() {
        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .postServerListOfActiveDriver(dataManager.getAdminId()!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_driver, this::handleError_driver)
        )
    }

    // handle sucess response of api call of get phone number
    private fun handleResponse_driver(response: ResponseFromServerDriverList) {
        hideDialogLoading()

        if (response.driver_detail.isNotEmpty()) {
            mListVehicle.clear()
            mListVehicle.addAll(response.driver_detail)
            val mCityAdapter = CustomadapterDriverList(response.driver_detail, this)
            dialog_custom1.recycler_view.adapter = mCityAdapter
            dialog_custom1.show()

        } else {
            showSnackBar(getString(R.string.nodriverfound))
        }

        mCompositeDisposable?.clear()


    }


    // handle failure response of api call of get phone number
    private fun handleError_driver(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }

    //geeting  total driver list
    private fun assignDriver() {
        showDialogLoading()
        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable?.add(
            ApiRequestClient.createREtrofitInstance()
                .assignDriver(mDriverId,mVehicleId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse_assigndriver, this::handleError_assigndriver)
        )
    }

    // handle sucess response of api call of get phone number
    private fun handleResponse_assigndriver(response: ResponseFromServerGeneric) {
        hideDialogLoading()
        showSnackBar(response.response_message)
        dialog_custom1.dismiss()

        mCompositeDisposable?.clear()


    }


    // handle failure response of api call of get phone number
    private fun handleError_assigndriver(error: Throwable) {
        hideDialogLoading()
        showSnackBar(error.localizedMessage)
        mCompositeDisposable?.clear()

    }


}
