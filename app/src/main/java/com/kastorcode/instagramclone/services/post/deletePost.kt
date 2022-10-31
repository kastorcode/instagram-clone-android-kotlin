package com.kastorcode.instagramclone.services.post

import com.google.firebase.database.*
import com.kastorcode.instagramclone.models.Post


private fun success (callback : (() -> Unit)?) {
    if (callback != null) {
        callback()
    }
}


private fun failure (
    errorCallback : ((exception : Exception) -> Unit)?, exception : Exception
) {
    if (errorCallback != null) {
        errorCallback(exception)
    }
}


private fun delete (
    post : Post, postRef : DatabaseReference,
    callback : (() -> Unit)?, errorCallback : ((exception : Exception) -> Unit)?
) {
    fun removeFromDatabase () {
        postRef.removeValue()
            .addOnSuccessListener {
                success(callback)
            }
            .addOnFailureListener { exception ->
                failure(errorCallback, exception)
            }
    }

    deletePostImage(post.getPostImage(),
        {
            deletePostComments(post.getPostId(),
                {
                    deletePostLikes(post.getPostId(),
                        {
                            deletePostSaves(post.getPostId(),
                                {
                                    removeFromDatabase()
                                },
                                errorCallback
                            )
                        },
                        errorCallback
                    )
                },
                errorCallback
            )
        },
        errorCallback
    )
}


fun deletePost (
    post : Post,
    callback : (() -> Unit)? = null, errorCallback : ((exception : Exception) -> Unit)? = null
) {
    FirebaseDatabase.getInstance().reference.child("Posts").child(post.getPostId())
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange (dataSnapshot : DataSnapshot) {
                if (dataSnapshot.exists()) {
                    delete(post, dataSnapshot.ref, callback, errorCallback)
                }
            }

            override fun onCancelled (error : DatabaseError) {}
        })
}


fun deletePost (
    post : Post, postRef : DatabaseReference,
    callback : (() -> Unit)? = null, errorCallback : ((exception : Exception) -> Unit)? = null
) {
    delete(post, postRef, callback, errorCallback)
}