package com.kastorcode.instagramclone.services.post

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kastorcode.instagramclone.services.notification.addNotification


fun likePost (
    postId : String, tag : String,
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

    if (tag == "Like") {
        FirebaseDatabase.getInstance().reference.child("Likes").child(postId)
            .child(firebaseUserUid).setValue(true)
            .addOnSuccessListener {
                addNotification(firebaseUserUid, postId, true, "liked your post")
                success()
            }
            .addOnFailureListener { exception ->
                failure(exception)
            }
    }
    else {
        FirebaseDatabase.getInstance().reference.child("Likes").child(postId)
            .child(firebaseUserUid).removeValue()
            .addOnSuccessListener {
                success()
            }
            .addOnFailureListener { exception ->
                failure(exception)
            }
    }
}