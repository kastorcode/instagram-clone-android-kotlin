package com.kastorcode.instagramclone.services.story

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


fun addStoryView (
    userId : String, storyId : String,
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

    FirebaseDatabase.getInstance().reference.child("Stories").child(userId).child(storyId)
        .child("views").child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(true)
        .addOnSuccessListener {
            success()
        }
        .addOnFailureListener { exception ->
            failure(exception)
        }
}