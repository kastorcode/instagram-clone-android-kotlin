package com.kastorcode.instagramclone.services.post

import android.annotation.SuppressLint
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.models.Post
import com.kastorcode.instagramclone.adapters.PostAdapter


fun getPost (
    postId : String, postList : MutableList<Post>, postAdapter : PostAdapter,
    callback : ((post : Post) -> Unit)? = null
) {
    FirebaseDatabase.getInstance().reference.child("Posts").child(postId)
        .addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange (dataSnapshot : DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val post = dataSnapshot.getValue(Post::class.java)
                    postList.clear()
                    postList.add(post!!)
                    postAdapter.notifyDataSetChanged()
                    if (callback != null) {
                        callback(post)
                    }
                }
            }

            override fun onCancelled (error : DatabaseError) {}
        })
}