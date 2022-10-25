package com.kastorcode.instagramclone.services.auth

import com.google.firebase.auth.FirebaseAuth


fun signIn (
    email : String, password : String,
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

    val map = mapOf(
        "email" to email,
        "password" to password
    )

    map.forEach { (key : String) ->
        if (map[key]!!.isEmpty()) {
            failure(Exception("$key is required"))
            return
        }
    }

    FirebaseAuth.getInstance().signInWithEmailAndPassword(map["email"]!!, map["password"]!!)
        .addOnSuccessListener {
            success()
        }
        .addOnFailureListener { exception ->
            failure(exception)
        }
}