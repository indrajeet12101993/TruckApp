package com.truckintransit.operator.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.truckintransit.operator.R
import com.truckintransit.operator.callbackInterface.InterfaceDriverSelect
import com.truckintransit.operator.pojo.driverlist.DriverDetail

class CustomadapterDriverList(private val cityList: List<DriverDetail>, private val listner: InterfaceDriverSelect) : RecyclerView.Adapter<CustomadapterDriverList.ViewHolder>()  {
    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomadapterDriverList.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.custom_city_list, parent, false)
        return ViewHolder(v)
    }


    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CustomadapterDriverList.ViewHolder, position: Int) {
        holder.bindItems(cityList[position],listner,position)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return cityList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(result: DriverDetail, listner: InterfaceDriverSelect,position: Int) {
            val textViewName = itemView.findViewById(R.id.tv_name) as TextView
            textViewName.text = result.name

            itemView.setOnClickListener {
                listner.driverselect(position)
            }

        }
    }
}