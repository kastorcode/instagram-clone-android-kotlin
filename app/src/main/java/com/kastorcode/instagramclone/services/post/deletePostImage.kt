package com.kastorcode.instagramclone.services.post

import com.google.firebase.storage.FirebaseStorage


fun deletePostImage (
    postImage : String,
    callback : (() -> Unit)? = null, errorCallback : ((exception : Exception) -> Unit)? = null
) {
    FirebaseStorage.getInstance().reference.child("posts-pictures")
        .child(postImage.split("posts-pictures%2F", "?")[1]).delete()
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