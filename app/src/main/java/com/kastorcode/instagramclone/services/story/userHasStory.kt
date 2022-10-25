package com.kastorcode.instagramclone.services.story

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.Models.Story


fun userHasStory (
    userId : String,
    callback : ((hasStory : Boolean) -> Unit)
) {
    FirebaseDatabase.getInstance().reference.child("Stories").child(userId)
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange (dataSnapshot : DataSnapshot) {
                var hasStory = false
                val timeCurrent = System.currentTimeMillis()
                for (snapshot in dataSnapshot.children) {
                    val story = snapshot.getValue(Story::class.java)
                    if (timeCurrent > story!!.getTimeStart() &&
                        timeCurrent < story.getTimeEnd()
                    ) {
                        hasStory = true
                        break
                    }
                    else {
                        deleteStory(story.getImageUrl(), snapshot.ref)
                    }
                }
                callback(hasStory)
            }

            override fun onCancelled (error : DatabaseError) {}
        })
}