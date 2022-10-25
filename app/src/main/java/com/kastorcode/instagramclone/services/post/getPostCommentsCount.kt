package com.kastorcode.instagramclone.services.post

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


fun getPostCommentsCount (
    postId : String,
    callback : ((count : Long) -> Unit)
) {
    FirebaseDatabase.getInstance().reference.child("Comments").child(postId)
        .addValueEventListener(object : ValueEventListener {
            override fun onDataChange (dataSnapshot : DataSnapshot) {
                if (dataSnapshot.exists()) {
                    callback(dataSnapshot.childrenCount)
                }
                else {
                    callback(0)
                }
            }

            override fun onCancelled (error : DatabaseError) {}
        })
}