package com.kastorcode.instagramclone.services.user

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


fun getUserFollowing (
    userId : String, idList : MutableList<String>,
    callback : (() -> Unit)? = null
) {
    FirebaseDatabase.getInstance().reference.child("Follow").child(userId)
        .child("Following").addValueEventListener(object : ValueEventListener {
            override fun onDataChange (dataSnapshot : DataSnapshot) {
                if (dataSnapshot.exists()) {
                    idList.clear()
                    for (snapshot in dataSnapshot.children) {
                        idList.add(snapshot.key!!)
                    }
                    if (callback != null) {
                        callback()
                    }
                }
            }

            override fun onCancelled (error : DatabaseError) {}
        })
}