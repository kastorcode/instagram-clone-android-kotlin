package com.kastorcode.instagramclone.services.story

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.Models.Story


fun userSawStory (
    userId : String,
    callback : ((sawStory : Boolean) -> Unit)
) {
    FirebaseDatabase.getInstance().reference.child("Stories").child(userId)
        .addValueEventListener(object : ValueEventListener {
            override fun onDataChange (dataSnapshot : DataSnapshot) {
                if (dataSnapshot.exists()) {
                    var sawStory = false
                    for (snapshot in dataSnapshot.children) {
                        if (!snapshot.child("views").child(FirebaseAuth.getInstance().currentUser!!.uid).exists() &&
                            System.currentTimeMillis() < snapshot.getValue(Story::class.java)!!.getTimeEnd()
                        ) {
                            sawStory = true
                            break
                        }
                    }
                    callback(sawStory)
                }
            }

            override fun onCancelled (error : DatabaseError) {}
        })
}