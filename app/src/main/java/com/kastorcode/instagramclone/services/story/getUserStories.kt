package com.kastorcode.instagramclone.services.story

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.models.Story


fun getUserStories (
    userId : String, imageList : MutableList<String>, storyList : MutableList<String>,
    callback : (() -> Unit)? = null
) {
    FirebaseDatabase.getInstance().reference.child("Stories").child(userId)
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange (dataSnapshot : DataSnapshot) {
                if (dataSnapshot.exists()) {
                    imageList.clear()
                    storyList.clear()
                    val timeCurrent = System.currentTimeMillis()
                    for (snapshot in dataSnapshot.children) {
                        val story = snapshot.getValue(Story::class.java)
                        if (timeCurrent > story!!.getTimeStart() && timeCurrent < story.getTimeEnd()) {
                            imageList.add(story.getImageUrl())
                            storyList.add(story.getStoryId())
                        }
                    }
                    if (callback != null) {
                        callback()
                    }
                }
            }

            override fun onCancelled (error : DatabaseError) {}
        })
}