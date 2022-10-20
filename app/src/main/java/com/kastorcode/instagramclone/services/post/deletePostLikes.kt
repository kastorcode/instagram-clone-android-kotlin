package com.kastorcode.instagramclone.services.post

import com.google.firebase.database.FirebaseDatabase


fun deletePostLikes (
    postId : String,
    callback : (() -> Unit)? = null, errorCallback : ((exception : Exception) -> Unit)? = null
) {
    FirebaseDatabase.getInstance().reference.child("Likes").child(postId).removeValue()
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