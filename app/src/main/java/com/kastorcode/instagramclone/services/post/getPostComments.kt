package com.kastorcode.instagramclone.services.post

import android.annotation.SuppressLint
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.adapters.CommentsAdapter
import com.kastorcode.instagramclone.Models.Comment


fun getPostComments (
    postId : String, commentsList : MutableList<Comment>, commentsAdapter : CommentsAdapter
) {
    FirebaseDatabase.getInstance().reference.child("Comments").child(postId)
        .addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange (dataSnapshot : DataSnapshot) {
                if (dataSnapshot.exists()) {
                    commentsList.clear()
                    for (snapshot in dataSnapshot.children) {
                        commentsList.add(snapshot.getValue(Comment::class.java)!!)
                    }
                    commentsAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled (error : DatabaseError) {}
        })
}