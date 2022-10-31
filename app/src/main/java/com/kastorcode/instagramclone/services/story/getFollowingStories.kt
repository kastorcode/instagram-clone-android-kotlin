package com.kastorcode.instagramclone.services.story

import android.annotation.SuppressLint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.models.Story
import com.kastorcode.instagramclone.adapters.StoryAdapter


@SuppressLint("NotifyDataSetChanged")
fun getFollowingStories (
    followingList : MutableList<String>, storyList : MutableList<Story>, storyAdapter : StoryAdapter,
    callback : (() -> Unit)? = null
) {
    storyList.clear()
    storyList.add(Story(FirebaseAuth.getInstance().currentUser!!.uid, "", "", 0, 0))
    val timeCurrent = System.currentTimeMillis()
    for (id in followingList) {
        FirebaseDatabase.getInstance().reference.child("Stories").child(id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange (dataSnapshot : DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (snapshot in dataSnapshot.children) {
                            val story = snapshot.getValue(Story::class.java)
                            if (timeCurrent > story!!.getTimeStart() &&
                                timeCurrent < story.getTimeEnd()
                            ) {
                                storyList.add(story)
                            }
                        }
                    }
                }

                override fun onCancelled (error : DatabaseError) {}
            })
    }
    storyAdapter.notifyDataSetChanged()
    if (callback != null) {
        callback()
    }
}