package com.kastorcode.instagramclone.services.user

import android.app.Activity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception


fun updateUserInfo (
    activity : Activity, username : String, fullname : String, bio : String,
    callback : (() -> Unit)? = null, errorCallback : ((exception : Exception) -> Unit)? = null
) {
    var map : Map<String, String> = mapOf(
        "username" to username,
        "fullname" to fullname,
        "bio" to bio
    )
    map = map.filterValues { value -> value.isNotEmpty() }
    FirebaseDatabase.getInstance().reference.child("Users")
        .child(FirebaseAuth.getInstance().currentUser!!.uid).updateChildren(map)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(activity, "Account has been updated successfully",
                    Toast.LENGTH_LONG).show()
                if (callback != null) {
                    callback()
                }
            }
            else {
                if (errorCallback != null) {
                    errorCallback(task.exception!!)
                }
            }
        }
}