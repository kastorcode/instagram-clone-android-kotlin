package com.kastorcode.instagramclone.services.post

import com.google.firebase.database.DatabaseReference


fun deletePost (
    postRef : DatabaseReference,
    callback : (() -> Unit)? = null, errorCallback : ((exception : Exception) -> Unit)? = null
) {
    postRef.removeValue().addOnCompleteListener { task ->
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