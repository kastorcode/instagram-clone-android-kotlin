package com.kastorcode.instagramclone.services.post

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.models.Post


fun deleteUserPosts (
    callback : (() -> Unit)? = null, errorCallback : ((exception : Exception) -> Unit)? = null
) {
    var wasSuccessful = true

    fun success () {
        if (wasSuccessful && callback != null) {
            callback()
        }
    }

    fun failure (exception : Exception) {
        wasSuccessful = false
        if (errorCallback != null) {
            errorCallback(exception)
        }
    }

    val firebaseUserUid = FirebaseAuth.getInstance().currentUser!!.uid

    FirebaseDatabase.getInstance().reference.child("Posts")
        .addValueEventListener(object : ValueEventListener {
            override fun onDataChange (dataSnapshot : DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    if (!wasSuccessful) { break }
                    val post = snapshot.getValue(Post::class.java)
                    if (post!!.getPublisher() == firebaseUserUid) {
                        deletePost(post, snapshot.ref, null
                        ) { exception -> failure(exception) }
                    }
                }
                success()
            }

            override fun onCancelled (error : DatabaseError) {}
        })
}