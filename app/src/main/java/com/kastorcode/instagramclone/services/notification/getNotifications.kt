package com.kastorcode.instagramclone.services.notification

import android.annotation.SuppressLint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.models.Notification
import com.kastorcode.instagramclone.adapters.NotificationAdapter


fun getNotifications (
    notificationList : MutableList<Notification>, notificationAdapter : NotificationAdapter,
    callback : (() -> Unit)? = null
) {
    FirebaseDatabase.getInstance().reference.child("Notifications")
        .child(FirebaseAuth.getInstance().currentUser!!.uid).addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange (dataSnapshot : DataSnapshot) {
                if (dataSnapshot.exists()) {
                    notificationList.clear()
                    for (snapshot in dataSnapshot.children) {
                        val notification = snapshot.getValue(Notification::class.java)
                        notificationList.add(notification!!)
                    }
                    notificationList.reverse()
                    notificationAdapter.notifyDataSetChanged()
                    if (callback != null) {
                        callback()
                    }
                }
            }

            override fun onCancelled (error : DatabaseError) {}
        })
}