package com.kastorcode.instagramclone.services.post

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


fun isPostLiked (
    postId : String,
    callback : ((isLiked : Boolean) -> Unit)
) {
    FirebaseDatabase.getInstance().reference.child("Likes").child(postId)
        .addValueEventListener(object : ValueEventListener {
            override fun onDataChange (dataSnapshot : DataSnapshot) {
                callback(dataSnapshot.child(FirebaseAuth.getInstance().currentUser!!.uid).exists())
            }

            override fun onCancelled (error : DatabaseError) {}
        })
}