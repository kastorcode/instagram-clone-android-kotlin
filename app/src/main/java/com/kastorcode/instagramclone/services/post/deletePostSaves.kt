package com.kastorcode.instagramclone.services.post

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


fun deletePostSaves (
    postId : String,
    callback : (() -> Unit)? = null, errorCallback : ((exception : Exception) -> Unit)? = null
) {
    var wasSuccessful = true
    FirebaseDatabase.getInstance().reference.child("Saves").addValueEventListener(object : ValueEventListener {
        override fun onDataChange (dataSnapshot : DataSnapshot) {
            for (snapshot in dataSnapshot.children) {
                if (!wasSuccessful) { break }
                for (child in snapshot.children) {
                    if (child.key == postId) {
                        child.ref.removeValue().addOnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                wasSuccessful = false
                                if (errorCallback != null) {
                                    errorCallback(task.exception!!)
                                }
                            }
                        }
                    }
                }
            }
            if (wasSuccessful && callback != null) {
                callback()
            }
        }

        override fun onCancelled (error: DatabaseError) {}
    })
}