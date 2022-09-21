package com.kastorcode.instagramclone.Services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


fun followUser (
    userUid: String, callback : (() -> Unit)? = null, errorCallback : (() -> Unit)? = null
) : Unit {
    val myUid = FirebaseAuth.getInstance().currentUser!!.uid
    FirebaseDatabase.getInstance().reference.child("Follow").child(myUid)
        .child("Following").child(userUid).setValue(true)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                FirebaseDatabase.getInstance().reference.child("Follow")
                    .child(userUid).child("Followers").child(myUid).setValue(true)
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