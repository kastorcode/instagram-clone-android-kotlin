package com.kastorcode.instagramclone.Services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


fun unfollowUser (
    userUid: String, callback : (() -> Unit)? = null, errorCallback : (() -> Unit)? = null
) : Unit {
    val myUid = FirebaseAuth.getInstance().currentUser!!.uid
    FirebaseDatabase.getInstance().reference.child("Follow").child(myUid)
        .child("Following").child(userUid).removeValue()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                FirebaseDatabase.getInstance().reference.child("Follow")
                    .child(userUid).child("Followers").child(myUid).removeValue()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            if (callback != null) {
                                callback()
                            }
                        }
                        else {
                            if (errorCallback != null) {
                                errorCallback()
                            }
                        }
                    }
            }
            else if (errorCallback != null) {
                errorCallback()
            }
        }
}