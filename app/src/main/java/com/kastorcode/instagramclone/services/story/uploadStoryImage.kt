package com.kastorcode.instagramclone.services.story

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage


fun uploadStoryImage (
    storyImageUri : Uri,
    callback : ((imageUrl : String) -> Unit)? = null, errorCallback : ((exception : Exception) -> Unit)? = null
) {
    fun success (imageUrl : String) {
        if (callback != null) {
            callback(imageUrl)
        }
    }

    fun failure (exception : Exception) {
        if (errorCallback != null) {
            errorCallback(exception)
        }
    }

    val fileRef = FirebaseStorage.getInstance().reference.child("stories-pictures")
        .child("${System.currentTimeMillis()}.jpg")
    fileRef.putFile(storyImageUri)
        .addOnSuccessListener {
            success(fileRef.downloadUrl.toString())
        }
        .addOnFailureListener { exception ->
            failure(exception)
        }
}