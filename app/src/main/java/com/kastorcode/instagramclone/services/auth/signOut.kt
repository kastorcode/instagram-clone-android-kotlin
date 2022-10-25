package com.kastorcode.instagramclone.services.auth

import android.app.Activity
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.kastorcode.instagramclone.activities.SignInActivity


fun signOut (activity : Activity) {
    FirebaseAuth.getInstance().signOut()
    val intent = Intent(activity, SignInActivity::class.java)
        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    activity.startActivity(intent)
    activity.finish()
}