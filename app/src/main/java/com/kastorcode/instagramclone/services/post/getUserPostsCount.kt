package com.kastorcode.instagramclone.services.post

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.models.Post


fun getUserPostsCount (
    userId : String,
    callback : ((count : Int) -> Unit)
) {
    FirebaseDatabase.getInstance().reference.child("Posts")
        .addValueEventListener(object : ValueEventListener {
            override fun onDataChange (dataSnapshot : DataSnapshot) {
                if (dataSnapshot.exists()) {
                    var count = 0
                    for (snapshot in dataSnapshot.children) {
                        if (snapshot.getValue(Post::class.java)!!.getPublisher() == userId) {
                            count++
                        }
                    }
                    callback(count)
                }
            }

            override fun onCancelled (error : DatabaseError) {}
        })
}