package com.kastorcode.instagramclone.services.user

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.lang.Exception


fun updateUserProfileImage (
    userImageUri : Uri?,
    callback : (() -> Unit)? = null, errorCallback : ((exception : Exception) -> Unit)? = null
) {
    if (userImageUri == null) {
        return
    }

    val firebaseUserUid = FirebaseAuth.getInstance().currentUser!!.uid

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

    fun updateImageUri (image : String) {
        val map : Map<String, String> = mapOf(
            "image" to image
        )
        FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserUid)
            .updateChildren(map)
            .addOnSuccessListener {
                success()
            }
            .addOnFailureListener { exception ->
                failure(exception)
            }
    }

    val fileRef = FirebaseStorage.getInstance().reference.child("profile-pictures")
        .child("$firebaseUserUid.jpg")

    fileRef.putFile(userImageUri)
        .addOnSuccessListener {
            fileRef.downloadUrl
                .addOnSuccessListener { uri ->
                    updateImageUri(uri.toString())
                }
                .addOnFailureListener { exception ->
                    failure(exception)
                }
        }
        .addOnFailureListener { exception ->
            failure(exception)
        }
}