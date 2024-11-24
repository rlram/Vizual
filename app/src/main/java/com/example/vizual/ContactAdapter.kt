package com.example.vizual

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter(private val list: ArrayList<User>): RecyclerView.Adapter<ContactAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvPersonName: TextView = view.findViewById(R.id.tvPersonName)
        val tvUserName: TextView = view.findViewById(R.id.tvUserName)
        val ivVideoCall: ImageView = view.findViewById(R.id.ivVideoCall)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_each_contact, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvPersonName.text = item.name
        holder.tvUserName.text = item.userName

    }
}