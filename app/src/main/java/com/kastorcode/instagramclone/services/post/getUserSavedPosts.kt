package com.kastorcode.instagramclone.services.post

import android.annotation.SuppressLint
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.models.Post
import com.kastorcode.instagramclone.adapters.MyImagesAdapter


fun getUserSavedPosts (
    userId : String, mySavedImages : MutableList<String>, savedPostList : MutableList<Post>,
    mySavedImagesAdapter : MyImagesAdapter,
    callback : (() -> Unit)? = null
) {
    fun getMySavedImages () {
        FirebaseDatabase.getInstance().reference.child("Posts")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange (dataSnapshot : DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        savedPostList.clear()
                        for (snapshot in dataSnapshot.children) {
                            val post = snapshot.getValue(Post::class.java)
                            for (id in mySavedImages) {
                                if (id == post!!.getPostId()) {
                                    savedPostList.add(post)
                                }
                            }
                        }
                        mySavedImagesAdapter.notifyDataSetChanged()
                        if (callback != null) {
                            callback()
                        }
                    }
                }

                override fun onCancelled (error : DatabaseError) {}
            })
    }

    FirebaseDatabase.getInstance().reference.child("Saves").child(userId)
        .addValueEventListener(object : ValueEventListener {
            override fun onDataChange (dataSnapshot : DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {
                        mySavedImages.add(snapshot.key!!)
                    }
                    getMySavedImages()
                }
            }

            override fun onCancelled (error : DatabaseError) {}
        })
}