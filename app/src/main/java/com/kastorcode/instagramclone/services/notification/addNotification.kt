package com.kastorcode.instagramclone.services.notification

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


fun addNotification (userId : String, postId : String, isPost : Boolean, text : String) {
    val map = HashMap<String, Any>()
    map["userId"] = userId
    map["postId"] = postId
    map["isPost"] = isPost
    map["text"] = text
    FirebaseDatabase.getInstance().reference.child("Notifications")
        .child(FirebaseAuth.getInstance().currentUser!!.uid).push().setValue(map)
}