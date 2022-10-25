package com.kastorcode.instagramclone.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.adapters.NotificationAdapter
import com.kastorcode.instagramclone.Models.Notification

import com.kastorcode.instagramclone.R
import kotlin.collections.ArrayList


class NotificationsFragment : Fragment() {

    private lateinit var fragmentNotificationsView : View
    private lateinit var notificationList : MutableList<Notification>
    private lateinit var notificationAdapter : NotificationAdapter


    override fun onCreateView (
        inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?
    ) : View {
        setProps(inflater, container)
        getNotifications()
        return fragmentNotificationsView
    }


    private fun setProps (inflater : LayoutInflater, container : ViewGroup?) {
        fragmentNotificationsView = inflater.inflate(R.layout.fragment_notifications, container, false)
        notificationList = ArrayList()
        notificationAdapter = NotificationAdapter(context!!, notificationList)
        val recyclerView = fragmentNotificationsView.findViewById<RecyclerView>(R.id.notifications_recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = notificationAdapter
    }


    private fun getNotifications () {
        FirebaseDatabase.getInstance().reference.child("Notifications")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange (dataSnapshot : DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        notificationList.clear()
                        for (snapshot in dataSnapshot.children) {
                            val notification = snapshot.getValue(Notification::class.java)
                            notificationList.add(notification!!)
                        }
                        notificationList.reverse()
                        notificationAdapter.notifyDataSetChanged()
                    }
                }

                override fun onCancelled (error : DatabaseError) {}
            })
    }
}