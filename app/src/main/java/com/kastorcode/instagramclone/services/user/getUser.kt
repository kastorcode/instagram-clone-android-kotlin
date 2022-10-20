package com.kastorcode.instagramclone.services.user

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.Models.User


fun getUser (userId : String, callback : ((user : User) -> Unit)) {
    FirebaseDatabase.getInstance().reference.child("Users")
        .child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange (snapshot : DataSnapshot) {
                if (snapshot.exists()) {
                    callback(snapshot.getValue(User::class.java)!!)
                }
            }

            override fun onCancelled (error : DatabaseError) {
            }
        })
}