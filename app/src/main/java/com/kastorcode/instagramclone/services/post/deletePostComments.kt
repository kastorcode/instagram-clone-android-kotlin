package com.kastorcode.instagramclone.services.post

import com.google.firebase.database.FirebaseDatabase


fun deletePostComments (
    postId : String,
    callback : (() -> Unit)? = null, errorCallback : ((exception : Exception) -> Unit)? = null
) {
    FirebaseDatabase.getInstance().reference.child("Comments").child(postId).removeValue()
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