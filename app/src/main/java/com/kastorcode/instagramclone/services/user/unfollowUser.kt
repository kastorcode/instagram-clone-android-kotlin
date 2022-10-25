package com.kastorcode.instagramclone.services.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


fun unfollowUser (
    userId: String,
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

    val firebaseUserUid = FirebaseAuth.getInstance().currentUser!!.uid

    FirebaseDatabase.getInstance().reference.child("Follow").child(firebaseUserUid)
        .child("Following").child(userId).removeValue()
        .addOnSuccessListener {
            FirebaseDatabase.getInstance().reference.child("Follow").child(userId)
                .child("Followers").child(firebaseUserUid).removeValue()
                .addOnSuccessListener {
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