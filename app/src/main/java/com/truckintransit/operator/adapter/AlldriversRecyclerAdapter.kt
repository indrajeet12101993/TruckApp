package com.truckintransit.operator.adapter

import android.app.ProgressDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.truckintransit.operator.R
import com.truckintransit.operator.callbackInterface.ListnerForNaviagtionItem
import com.truckintransit.operator.constants.AppConstants
import com.truckintransit.operator.newtworking.ApiRequestClient
import com.truckintransit.operator.pojo.ResponseFromServerGeneric
import com.truckintransit.operator.pojo.driverlist.DriverDetail
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_add_drivers.view.*

class AlldriversRecyclerAdapter(var mcontext: Context, var mList:ArrayList<DriverDetail>, var mlistner: ListnerForNaviagtionItem) : RecyclerView.Adapter<AlldriversRecyclerAdapter.ViewHolder>() {



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(mcontext,mList[position], mlistner,  position)
    }

    override fun getItemCount(): Int = mList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_add_drivers, parent, false)

        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var mCompositeDisposable: CompositeDisposable? = null
        lateinit var dialog: ProgressDialog
       private lateinit var mcontext1:Context
        fun bind(mcontext: Context,item: DriverDetail, listener: ListnerForNaviagtionItem, position: Int) {
            mcontext1=mcontext

           // 1-- onroad 2-- active 3--offline  4-- pending

            if(item.is_assigned=="1"){
                itemView.tv_status.text = mcontext.getString(R.string.onroad)
                itemView.relative.setBackgroundResource(R.drawable.square_online)
            }
            if(item.is_assigned=="2"){
                itemView.tv_status.text = mcontext.getString(R.string.active)
                itemView.relative.setBackgroundResource(R.drawable.square_blue)

            }
            if(item.is_assigned=="3"){
                itemView.tv_status.text = mcontext.getString(R.string.offfline)
                itemView.relative.setBackgroundResource(R.drawable.square_offline)
            }
            if(item.is_assigned=="4"){
                itemView.tv_status.text = mcontext.getString(R.string.pending)
                itemView.relative.setBackgroundResource(R.drawable.square)
            }


            itemView.tv_profile_name.text=item.name

            //1-- inactvie
            //0-- active

            if(item.active=="0"){
                itemView.tv_deactvite.text=itemView.context.getString(R.string.deactivatedriver)

                itemView.switch_toogle.isChecked=true
            }else{
                itemView.tv_deactvite.text=itemView.context.getString(R.string.activatedriver)

                itemView.switch_toogle.isChecked=false

            }

            itemView.switch_toogle.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    blockingDriver(item.id,"0")
                    itemView.tv_deactvite.text=itemView.context.getString(R.string.deactivatedriver)



                } else {
                    blockingDriver(item.id,"1")
                    itemView.tv_deactvite.text=itemView.context.getString(R.string.activatedriver)
                }
            }

            if(item.photo==null){

            }else{
                Glide.with(mcontext).load(item.photo).into(itemView.iv_profile_pic)

            }





            itemView.setOnClickListener {
                listener.itemSelcectPosition(position)
            }
        }
        fun blockingDriver(id: String, blokedsttus: String) {

            showDialogLoading()


            mCompositeDisposable = CompositeDisposable()

            mCompositeDisposable?.add(
                ApiRequestClient.createREtrofitInstance()
                    .driverBlocked(id,blokedsttus)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponseUser_Photo, this::handleError_user_photo)
            )
        }


        // handle sucess response of api call registration
        private fun handleResponseUser_Photo(response: ResponseFromServerGeneric) {
            hideDialogLoading()
            mCompositeDisposable?.clear()

        }


        // handle failure response of api call registration
        private fun handleError_user_photo(error: Throwable) {

            hideDialogLoading()
            mCompositeDisposable?.clear()

        }

        fun showDialogLoading() {

            dialog = ProgressDialog(mcontext1)
            dialog.setMessage("Please wait...")
            dialog.setTitle("Loading...")
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(false)
            dialog.isIndeterminate = true
            if (!dialog.isShowing) {
                dialog.show()
            }

        }

        fun hideDialogLoading() {


            if (dialog != null && dialog.isShowing)
                dialog.cancel()


        }

    }



}