package com.truckintransit.operator.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.truckintransit.operator.R
import com.truckintransit.operator.callbackInterface.InterfaceAssignDriver
import com.truckintransit.operator.callbackInterface.ListnerForNaviagtionItem
import com.truckintransit.operator.pojo.vehicleList.VehicleDetail
import kotlinx.android.synthetic.main.custom_allvehicle_list.view.*


class AllVehiclesCarAdapter(var mcontext: Context, var mList:ArrayList<VehicleDetail>, var mlistner: ListnerForNaviagtionItem, var mlistnerassignDriver: InterfaceAssignDriver) : RecyclerView.Adapter<AllVehiclesCarAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(mcontext,mList[position], mlistner,mlistnerassignDriver, position)
    }

    override fun getItemCount(): Int = mList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_allvehicle_list, parent, false)

        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(mcontext: Context, item: VehicleDetail, listener: ListnerForNaviagtionItem,  mlistnerassignDriver: InterfaceAssignDriver,position: Int) {

            itemView.tv_vehicle_name.text=item.vehicle_name
            itemView.tv_capacity.text=item.capacity
            itemView.tv_size.text=item.size
            itemView.tv_registartion_number.text=item.reg_no

            if(item.vehicle_photo==null){

            }else{
                Glide.with(mcontext).load(item.vehicle_photo).into(itemView.iv_profile_pic)

            }
            if(item.drivername!=null){
                itemView.tv_assigndriver.text= mcontext.getString(R.string.assign_driver)+" to  "+item.drivername
            }

            itemView.tv_assigndriver.setOnClickListener {
                mlistnerassignDriver.selectVehicle(position)

            }


            itemView.setOnClickListener {
                listener.itemSelcectPosition(position)
            }
        }
    }
}