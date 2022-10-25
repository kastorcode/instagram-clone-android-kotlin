package com.kastorcode.instagramclone.services.user

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener


fun userIsFollowing (
    followingRef : DatabaseReference, userId : String,
    callback : ((isFollowing : Boolean) -> Unit)
) {
    followingRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange (dataSnapshot : DataSnapshot) {
            callback(dataSnapshot.child(userId).exists())
        }

        override fun onCancelled (error : DatabaseError) {}
    })
}