package com.kastorcode.instagramclone.services.story

import com.google.firebase.storage.FirebaseStorage


fun deleteStoryImage (
    imageUrl : String,
    callback : (() -> Unit)? = null, errorCallback : ((exception : Exception) -> Unit)? = null
) {
    FirebaseStorage.getInstance().reference.child("stories-pictures")
        .child(imageUrl.split("stories-pictures%2F", "?")[1]).delete()
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