package com.kastorcode.instagramclone.services.story

import com.google.firebase.database.DatabaseReference


fun deleteStory (
    storyRef : DatabaseReference,
    callback : (() -> Unit)? = null, errorCallback : ((exception : Exception) -> Unit)? = null
) {
    storyRef.removeValue().addOnCompleteListener { task ->
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