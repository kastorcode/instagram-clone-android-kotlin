package com.kastorcode.instagramclone.services.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException


fun deleteUserProfileImage (
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
    FirebaseStorage.getInstance().reference.child("profile-pictures")
        .child("${FirebaseAuth.getInstance().currentUser!!.uid}.jpg").delete()
        .addOnSuccessListener {
            success()
        }
        .addOnFailureListener { exception ->
            if ((exception as StorageException).httpResultCode == 404) {
                success()
            }
            else {
                failure(exception)
            }
        }
}