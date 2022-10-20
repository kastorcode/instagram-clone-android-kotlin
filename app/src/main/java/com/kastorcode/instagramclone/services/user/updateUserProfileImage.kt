package com.kastorcode.instagramclone.services.user

import android.net.Uri
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.lang.Exception


fun updateUserProfileImage (
    userImageUri : Uri?,
    callback : (() -> Unit)? = null, errorCallback : ((exception : Exception) -> Unit)? = null
) {
    if (userImageUri == null) {
        return
    }
    val firebaseUserUid = FirebaseAuth.getInstance().currentUser!!.uid
    val fileRef = FirebaseStorage.getInstance().reference.child("profile-pictures")
        .child("$firebaseUserUid.jpg")
    fileRef.putFile(userImageUri)
        .continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (task.isSuccessful) {
                return@Continuation fileRef.downloadUrl
            }
            throw task.exception!!
        }).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val map : Map<String, String> = mapOf(
                    "image" to task.result.toString()
                )
                FirebaseDatabase.getInstance().reference.child("Users")
                    .child(firebaseUserUid).updateChildren(map).addOnCompleteListener { task ->
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
            else {
                if (errorCallback != null) {
                    errorCallback(task.exception!!)
                }
            }
        }
}