package com.kastorcode.instagramclone.services.post

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


fun savePost (
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

    if (tag == "Save") {
        FirebaseDatabase.getInstance().reference.child("Saves").child(firebaseUserUid)
            .child(postId).setValue(true)
            .addOnSuccessListener {
                success()
            }
            .addOnFailureListener { exception ->
                failure(exception)
            }
    }
    else {
        FirebaseDatabase.getInstance().reference.child("Saves").child(firebaseUserUid)
            .child(postId).removeValue()
            .addOnSuccessListener {
                success()
            }
            .addOnFailureListener { exception ->
                failure(exception)
            }
    }
}