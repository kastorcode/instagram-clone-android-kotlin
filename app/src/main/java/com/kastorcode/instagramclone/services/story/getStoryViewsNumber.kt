package com.kastorcode.instagramclone.services.story

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


fun getStoryViewsNumber (
    userId : String, storyId : String,
    callback : ((count : String) -> Unit)
) {
    FirebaseDatabase.getInstance().reference.child("Stories").child(userId).child(storyId)
        .child("views").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange (dataSnapshot : DataSnapshot) {
                if (dataSnapshot.exists()) {
                    callback(dataSnapshot.childrenCount.toString())
                }
            }

            override fun onCancelled (error : DatabaseError) {}
        })
}