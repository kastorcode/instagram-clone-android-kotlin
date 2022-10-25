package com.kastorcode.instagramclone.services.post

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage


fun uploadPostImage (
    postImageUri : Uri,
    callback : ((postImage : String) -> Unit)? = null, errorCallback : ((exception : Exception) -> Unit)? = null
) {
    fun success (postImage : String) {
        if (callback != null) {
            callback(postImage)
        }
    }

    fun failure (exception : Exception) {
        if (errorCallback != null) {
            errorCallback(exception)
        }
    }

    val fileRef = FirebaseStorage.getInstance().reference.child("posts-pictures")
        .child("${System.currentTimeMillis()}.jpg")
    fileRef.putFile(postImageUri)
        .addOnSuccessListener {
            fileRef.downloadUrl
                .addOnSuccessListener { uri ->
                    success(uri.toString())
                }
                .addOnFailureListener { exception ->
                    failure(exception)
                }
        }
        .addOnFailureListener { exception ->
            failure(exception)
        }
}