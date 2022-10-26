package com.kastorcode.instagramclone.services.post

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.Models.Post


fun deleteUserPosts (
    callback : (() -> Unit)? = null, errorCallback : ((exception : Exception) -> Unit)? = null
) {
    var wasSuccessful = true
    val firebaseUserUid = FirebaseAuth.getInstance().currentUser!!.uid
    FirebaseDatabase.getInstance().reference.child("Posts")
        .addValueEventListener(object : ValueEventListener {
            override fun onDataChange (dataSnapshot : DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    if (!wasSuccessful) { break }
                    val post = snapshot.getValue(Post::class.java)
                    if (post!!.getPublisher() == firebaseUserUid) {
                        deletePostImage(
                            post.getPostImage(),
                            {
                                deletePostComments(
                                    post.getPostId(),
                                    {
                                        deletePostLikes(
                                            post.getPostId(),
                                            {
                                                deletePostSaves(
                                                    post.getPostId(),
                                                    {
                                                        deletePost(
                                                            snapshot.ref,
                                                            null
                                                        ) { exception ->
                                                            wasSuccessful = false
                                                            if (errorCallback != null) {
                                                                errorCallback(exception)
                                                            }
                                                        }
                                                    },
                                                    { exception ->
                                                        wasSuccessful = false
                                                        if (errorCallback != null) {
                                                            errorCallback(exception)
                                                        }
                                                    }
                                                )
                                            },
                                            { exception ->
                                                wasSuccessful = false
                                                if (errorCallback != null) {
                                                    errorCallback(exception)
                                                }
                                            }
                                        )
                                    },
                                    { exception ->
                                        wasSuccessful = false
                                        if (errorCallback != null) {
                                            errorCallback(exception)
                                        }
                                    }
                                )
                            },
                            { exception ->
                                wasSuccessful = false
                                if (errorCallback != null) {
                                    errorCallback(exception)
                                }
                            }
                        )
                    }
                }
                if (wasSuccessful && callback != null) {
                    callback()
                }
            }

            override fun onCancelled (error : DatabaseError) {}
        })
}