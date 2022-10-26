package com.kastorcode.instagramclone.services.post

import android.annotation.SuppressLint
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.models.Post
import com.kastorcode.instagramclone.adapters.MyImagesAdapter


fun getUserPosts (
    userId : String, postList : MutableList<Post>, myImagesAdapter : MyImagesAdapter,
    callback : (() -> Unit)? = null
) {
    FirebaseDatabase.getInstance().reference.child("Posts")
        .addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange (dataSnapshot : DataSnapshot) {
                if (dataSnapshot.exists()) {
                    postList.clear()
                    for (snapshot in dataSnapshot.children) {
                        val post = snapshot.getValue(Post::class.java)
                        if (post!!.getPublisher() == userId) {
                            postList.add(post)
                        }
                    }
                    postList.reverse()
                    myImagesAdapter.notifyDataSetChanged()
                    if (callback != null) {
                        callback()
                    }
                }
            }

            override fun onCancelled (error : DatabaseError) {}
        })
}