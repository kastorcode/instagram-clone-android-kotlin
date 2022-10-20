package com.kastorcode.instagramclone.services.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


fun deleteUserFollow (
    callback : (() -> Unit)? = null, errorCallback : ((exception : Exception) -> Unit)? = null
) {
    var wasSuccessful = true
    val firebaseUserUid = FirebaseAuth.getInstance().currentUser!!.uid
    val followRef = FirebaseDatabase.getInstance().reference.child("Follow")
    val userFollowRef = followRef.child(firebaseUserUid)

    fun removeUserFollowRef () {
        if (wasSuccessful) {
            userFollowRef.removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (callback != null) {
                        callback()
                    }
                }
                else {
                    wasSuccessful = false
                    if (errorCallback != null) {
                        errorCallback(task.exception!!)
                    }
                }
            }
        }
    }

    fun removeChildren (dataSnapshot : DataSnapshot) {
        for (snapshot in dataSnapshot.children) {
            if (!wasSuccessful) { break }
            val child = followRef.child(snapshot.key!!)
            child.child("Followers").child(firebaseUserUid)
                .removeValue().addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        wasSuccessful = false
                        if (errorCallback != null) {
                            errorCallback(task.exception!!)
                        }
                    }
                }
            child.child("Following").child(firebaseUserUid)
                .removeValue().addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        wasSuccessful = false
                        if (errorCallback != null) {
                            errorCallback(task.exception!!)
                        }
                    }
                }
        }
    }

    userFollowRef.child("Followers").addValueEventListener(object : ValueEventListener {
        override fun onDataChange (dataSnapshot : DataSnapshot) {
            removeChildren(dataSnapshot)

            userFollowRef.child("Following").addValueEventListener(object : ValueEventListener {
                override fun onDataChange (dataSnapshot : DataSnapshot) {
                    removeChildren(dataSnapshot)
                    removeUserFollowRef()
                }

                override fun onCancelled (error : DatabaseError) {}
            })
        }

        override fun onCancelled (error : DatabaseError) {}
    })
}