package com.kastorcode.instagramclone.Services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


fun addNotification (myUid : String, userId : String) {
    val notificationMap = HashMap<String, Any>()
    notificationMap["userId"] = userId
    notificationMap["postId"] = ""
    notificationMap["isPost"] = false
    notificationMap["text"] = "started following you"
    FirebaseDatabase.getInstance().reference.child("Notifications")
        .child(myUid).push().setValue(notificationMap)
}


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
                            addNotification(myUid, userUid)
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