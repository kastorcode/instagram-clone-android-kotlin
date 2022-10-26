package com.kastorcode.instagramclone.services.user

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


fun getUserFollowingCount (
    userId : String,
    callback : ((count : Long) -> Unit)
) {
    FirebaseDatabase.getInstance().reference.child("Follow").child(userId)
        .child("Following").addValueEventListener(object : ValueEventListener {
            override fun onDataChange (dataSnapshot : DataSnapshot) {
                if (dataSnapshot.exists()) {
                    callback(dataSnapshot.childrenCount)
                }
            }

            override fun onCancelled (error : DatabaseError) {}
        })
}