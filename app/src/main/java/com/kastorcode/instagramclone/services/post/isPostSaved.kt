package com.kastorcode.instagramclone.services.post

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


fun isPostSaved (
    postId : String,
    callback : ((isSaved : Boolean) -> Unit)
) {
    FirebaseDatabase.getInstance().reference.child("Saves")
        .child(FirebaseAuth.getInstance().currentUser!!.uid).child(postId)
        .addValueEventListener(object : ValueEventListener {
            override fun onDataChange (dataSnapshot : DataSnapshot) {
                callback(dataSnapshot.exists())
            }

            override fun onCancelled (error : DatabaseError) {}
        })
}