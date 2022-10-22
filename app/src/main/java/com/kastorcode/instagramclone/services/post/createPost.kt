package com.kastorcode.instagramclone.services.post

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


fun createPost (
    postImage : String, description : String,
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

    val postsRef = FirebaseDatabase.getInstance().reference.child("Posts")
    val map : Map<String, String> = mapOf(
        "postId" to postsRef.push().key!!,
        "postImage" to postImage,
        "publisher" to FirebaseAuth.getInstance().currentUser!!.uid,
        "description" to description
    )
    postsRef.child(map["postId"]!!).updateChildren(map)
        .addOnSuccessListener {
            success()
        }
        .addOnFailureListener { exception ->
            failure(exception)
        }
}