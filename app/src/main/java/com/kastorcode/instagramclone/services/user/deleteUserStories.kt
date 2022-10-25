package com.kastorcode.instagramclone.services.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.Models.Story
import com.kastorcode.instagramclone.services.story.deleteStory


fun deleteUserStories (
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

    FirebaseDatabase.getInstance().reference.child("Stories").child(FirebaseAuth.getInstance().currentUser!!.uid)
        .addValueEventListener(object : ValueEventListener {
            override fun onDataChange (dataSnapshot : DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    if (!wasSuccessful) { break }
                    val story = snapshot.getValue(Story::class.java)
                    deleteStory(
                        story!!.getImageUrl(),
                        snapshot.ref,
                        null
                    ) { exception ->
                        failure(exception)
                    }
                }
                success()
            }

            override fun onCancelled (error : DatabaseError) {}
        })
}