package com.kastorcode.instagramclone.services.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kastorcode.instagramclone.services.notification.addNotification


fun followUser (
    userId : String, text : String,
    callback : (() -> Unit)? = null, errorCallback : ((exception : Exception) -> Unit)? = null
) {
    fun success () {
        if (callback != null) {
            callback()
        }
    }

    fun failure (exception : Exception) {
        if (errorCallback != null) {
            errorCallback(exception)
        }
    }

    if (text == "Follow") {
        val firebaseUserUid = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseDatabase.getInstance().reference.child("Follow").child(firebaseUserUid)
            .child("Following").child(userId).setValue(true)
            .addOnSuccessListener {
                FirebaseDatabase.getInstance().reference.child("Follow").child(userId)
                    .child("Followers").child(firebaseUserUid).setValue(true)
                    .addOnSuccessListener {
                        addNotification(userId, "", false, "started following you")
                        success()
                    }
                    .addOnFailureListener { exception ->
                        failure(exception)
                    }
            }
            .addOnFailureListener { exception ->
                failure(exception)
            }
    }
    else {
        unfollowUser(userId, callback, errorCallback)
    }
}