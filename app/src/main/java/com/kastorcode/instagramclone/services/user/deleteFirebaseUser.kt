package com.kastorcode.instagramclone.services.user

import com.google.firebase.auth.FirebaseAuth


fun deleteFirebaseUser (
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

    FirebaseAuth.getInstance().currentUser!!.delete()
        .addOnSuccessListener {
            success()
        }
        .addOnFailureListener { exception ->
            failure(exception)
        }
}