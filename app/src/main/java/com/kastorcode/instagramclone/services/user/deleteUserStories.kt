package com.kastorcode.instagramclone.services.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.Models.Story
import com.kastorcode.instagramclone.services.story.deleteStory
import com.kastorcode.instagramclone.services.story.deleteStoryImage


fun deleteUserStories (
    callback : (() -> Unit)? = null, errorCallback : ((exception : Exception) -> Unit)? = null
) {
    var wasSuccessful = true
    FirebaseDatabase.getInstance().reference.child("Stories").child(FirebaseAuth.getInstance().currentUser!!.uid)
        .addValueEventListener(object : ValueEventListener {
            override fun onDataChange (dataSnapshot : DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    if (!wasSuccessful) { break }
                    val story = snapshot.getValue(Story::class.java)
                    deleteStoryImage(
                        story!!.getImageUrl(),
                        {
                            deleteStory(
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
                }
                if (wasSuccessful && callback != null) {
                    callback()
                }
            }

            override fun onCancelled (error : DatabaseError) {}
        })
}