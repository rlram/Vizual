package com.example.vizual

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton
import com.zegocloud.uikit.service.defines.ZegoUIKitUser
import java.util.Collections

class ContactAdapter(private val list: ArrayList<User>): RecyclerView.Adapter<ContactAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvPersonName: TextView = view.findViewById(R.id.tvPersonName)
        val tvUserName: TextView = view.findViewById(R.id.tvUserName)
        val btnVideoCall: ZegoSendCallInvitationButton = view.findViewById(R.id.btnVideoCall)
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

        holder.btnVideoCall.setIsVideoCall(true)
        holder.btnVideoCall.resourceID = "zego_uikit_call"
        holder.btnVideoCall.setInvitees(Collections.singletonList(ZegoUIKitUser(item.userName, item.userName)))
    }
}