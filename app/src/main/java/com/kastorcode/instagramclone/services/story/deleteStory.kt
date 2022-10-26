package com.kastorcode.instagramclone.services.story

import com.google.firebase.database.*
import com.kastorcode.instagramclone.models.Story


private fun success (callback : (() -> Unit)?) {
    if (callback != null) {
        callback()
    }
}


private fun failure (errorCallback : ((exception : Exception) -> Unit)?, exception : Exception) {
    if (errorCallback != null) {
        errorCallback(exception)
    }
}


fun deleteStory (
    imageUrl : String, storyRef : DatabaseReference,
    callback : (() -> Unit)? = null, errorCallback : ((exception : Exception) -> Unit)? = null
) {
    deleteStoryImage(
        imageUrl,
        {
            storyRef.removeValue()
                .addOnSuccessListener {
                    success(callback)
                }
                .addOnFailureListener { exception ->
                    failure(errorCallback, exception)
                }
        },
        errorCallback
    )
}


fun deleteStory (
    userId: String, storyId : String,
    callback : (() -> Unit)? = null, errorCallback : ((exception : Exception) -> Unit)? = null
) {
    val storyRef = FirebaseDatabase.getInstance().reference.child("Stories").child(userId).child(storyId)
    storyRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange (dataSnapshot : DataSnapshot) {
            if (dataSnapshot.exists()) {
                val story = dataSnapshot.getValue(Story::class.java)
                deleteStoryImage(
                    story!!.getImageUrl(),
                    {
                        storyRef.removeValue()
                            .addOnSuccessListener {
                                success(callback)
                            }
                            .addOnFailureListener { exception ->
                                failure(errorCallback, exception)
                            }
                    },
                    errorCallback
                )
            }
        }

        override fun onCancelled (error : DatabaseError) {}
    })
}