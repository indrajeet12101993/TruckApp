package com.truckintransit.operator.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.truckintransit.operator.R
import com.truckintransit.operator.callbackInterface.ListnerForNaviagtionItem
import kotlinx.android.synthetic.main.item.view.*

class NaviagtionDrawerRecyclerAdapter(var mcontext:Context,var mList:ArrayList<String>,var mlistner: ListnerForNaviagtionItem) : RecyclerView.Adapter<NaviagtionDrawerRecyclerAdapter.ViewHolder>() {



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(mcontext,mList[position], mlistner,  position)
    }

    override fun getItemCount(): Int = mList.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)

        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(mcontext: Context,item: String, listener: ListnerForNaviagtionItem, position: Int) {

            itemView.tv_item.text = item
         if(position%2==0){
             itemView.linear_layout.setBackgroundColor(ContextCompat.getColor(mcontext,R.color.navigation_text_colour))
         }else{
             itemView.linear_layout.setBackgroundColor(ContextCompat.getColor(mcontext,R.color.white))
         }


            itemView.setOnClickListener {
                listener.itemSelcectPosition(position)
            }
        }
    }
}