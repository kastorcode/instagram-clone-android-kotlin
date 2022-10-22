package com.kastorcode.instagramclone.services.story

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue


fun createStory (
    imageUrl : String,
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

    val firebaseUserUid = FirebaseAuth.getInstance().currentUser!!.uid
    val storiesRef = FirebaseDatabase.getInstance().reference
        .child("Stories").child(firebaseUserUid)
    val map : Map<String, Any> = mapOf(
        "userId" to firebaseUserUid,
        "storyId" to storiesRef.push().key!!,
        "imageUrl" to imageUrl,
        "timeStart" to ServerValue.TIMESTAMP,
        "timeEnd" to System.currentTimeMillis() + 86400000 // 1 day
    )
    storiesRef.child(map["storyId"].toString()).updateChildren(map)
        .addOnSuccessListener {
            success()
        }
        .addOnFailureListener { exception ->
            failure(exception)
        }
}