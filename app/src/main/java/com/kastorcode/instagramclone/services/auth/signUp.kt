package com.kastorcode.instagramclone.services.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


fun signUp (
    fullname : String, username : String, email : String, password : String,
    callback : (() -> Unit)? = null, errorCallback : ((exception : Exception) -> Unit)? = null
) {
    fun success () {
        if (callback != null) {
            callback()
        }
    }

    fun failure (exception : Exception) {
        if (errorCallback != null) {
            errorCallback(exception)
        }
    }

    fun followMyself (userId : String) {
        FirebaseDatabase.getInstance().reference.child("Follow").child(userId)
            .child("Following").child(userId).setValue(true)
            .addOnSuccessListener {
                success()
            }
            .addOnFailureListener { exception ->
                failure(exception)
            }
    }

    fun saveUserIntoDatabase (userId : String, map : MutableMap<String, String>) {
        map.remove("password")
        map["uid"] = userId
        map["bio"] = "Hey, I am using an Instagram Clone by <kastor.code/>"
        map["image"] = "https://firebasestorage.googleapis.com/v0/b/instagram-clone-39239.appspot.com/o/default-images%2Fprofile.png?alt=media"
        FirebaseDatabase.getInstance().reference.child("Users").child(userId)
            .setValue(map)
            .addOnSuccessListener {
                followMyself(userId)
            }
            .addOnFailureListener { exception ->
                failure(exception)
            }
    }

    val map = mutableMapOf(
        "fullname" to fullname,
        "username" to username,
        "email" to email,
        "password" to password
    )

    map.forEach { (key : String) ->
        if (map[key]!!.isEmpty()) {
            failure(Exception("$key is required"))
            return
        }
    }

    FirebaseAuth.getInstance().createUserWithEmailAndPassword(map["email"]!!, map["password"]!!)
        .addOnSuccessListener { authResult ->
            saveUserIntoDatabase(authResult.user!!.uid, map)
        }
        .addOnFailureListener { exception ->
            failure(exception)
        }
}