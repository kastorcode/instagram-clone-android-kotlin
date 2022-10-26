package com.kastorcode.instagramclone.services.notification

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


fun deleteUserNotifications (
    callback : (() -> Unit)? = null, errorCallback : ((exception : Exception) -> Unit)? = null
) {
    FirebaseDatabase.getInstance().reference.child("Notifications")
        .child(FirebaseAuth.getInstance().currentUser!!.uid).removeValue()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (callback != null) {
                    callback()
                }
            }
            else {
                if (errorCallback != null) {
                    errorCallback(task.exception!!)
                }
            }
        }
}