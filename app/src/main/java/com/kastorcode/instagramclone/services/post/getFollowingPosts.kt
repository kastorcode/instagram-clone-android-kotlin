package com.kastorcode.instagramclone.services.post

import android.annotation.SuppressLint
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.Models.Post
import com.kastorcode.instagramclone.adapters.PostAdapter


fun getFollowingPosts (
    followingList : MutableList<String>, postList : MutableList<Post>, postAdapter : PostAdapter,
    callback : (() -> Unit)? = null
) {
    FirebaseDatabase.getInstance().reference.child("Posts")
        .addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange (dataSnapshot : DataSnapshot) {
                postList.clear()
                for (snapshot in dataSnapshot.children) {
                    val post = snapshot.getValue(Post::class.java)
                    for (following in followingList) {
                        if (post!!.getPublisher() == following) {
                            postList.add(post)
                        }
                    }
                }
                postAdapter.notifyDataSetChanged()
                if (callback != null) {
                    callback()
                }
            }

            override fun onCancelled (error : DatabaseError) {}
        })
}