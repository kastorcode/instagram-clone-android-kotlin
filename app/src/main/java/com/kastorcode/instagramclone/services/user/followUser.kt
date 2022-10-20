package com.kastorcode.instagramclone.services.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kastorcode.instagramclone.services.notification.addNotification


fun followUser (
    userId: String, callback : (() -> Unit)? = null, errorCallback : (() -> Unit)? = null
) : Unit {
    val myId = FirebaseAuth.getInstance().currentUser!!.uid
    FirebaseDatabase.getInstance().reference.child("Follow").child(myId)
        .child("Following").child(userId).setValue(true)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                FirebaseDatabase.getInstance().reference.child("Follow")
                    .child(userId).child("Followers").child(myId).setValue(true)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            if (callback != null) {
                                callback()
                            }
                            addNotification(userId, "", false, "started following you")
                        }
                        else if (errorCallback != null) {
                            errorCallback()
                        }
                    }
            }
            else if (errorCallback != null) {
                errorCallback()
            }
        }
}