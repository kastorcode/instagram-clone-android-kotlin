package com.kastorcode.instagramclone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kastorcode.instagramclone.adapters.NotificationAdapter
import com.kastorcode.instagramclone.models.Notification
import com.kastorcode.instagramclone.R
import com.kastorcode.instagramclone.services.notification.deleteUserNotifications
import com.kastorcode.instagramclone.services.notification.getNotifications
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlin.collections.ArrayList


class NotificationsFragment : Fragment() {

    private lateinit var fragmentNotificationsView : View
    private lateinit var notificationList : MutableList<Notification>
    private lateinit var notificationAdapter : NotificationAdapter


    override fun onCreateView (
        inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?
    ) : View {
        setProps(inflater, container)
        getNotifications(notificationList, notificationAdapter)
        setClickListeners()
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


    private fun setClickListeners () {
        notifications_clean_btn.setOnClickListener {
            deleteUserNotifications()
        }
    }
}