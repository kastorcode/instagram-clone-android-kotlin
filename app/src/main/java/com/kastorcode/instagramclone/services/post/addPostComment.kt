package com.kastorcode.instagramclone.services.post

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kastorcode.instagramclone.services.notification.addNotification


fun addPostComment (
    postId : String, comment : String,
    callback : (() -> Unit)? = null, errorCallback : ((exception : Exception) -> Unit)? = null
) {
    val map = HashMap<String, String>()
    map["comment"] = comment
    map["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid

    fun success () {
        addNotification(map["publisher"]!!, postId, true, "commented: $comment")
        if (callback != null) {
            callback()
        }
    }

    fun failure (exception : Exception) {
        if (errorCallback != null) {
            errorCallback(exception)
        }
    }

    FirebaseDatabase.getInstance().reference.child("Comments").child(postId).push()
        .setValue(map)
        .addOnSuccessListener {
            success()
        }
        .addOnFailureListener { exception ->
            failure(exception)
        }
}