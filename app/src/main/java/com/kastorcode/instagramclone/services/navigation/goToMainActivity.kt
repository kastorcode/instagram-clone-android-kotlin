package com.kastorcode.instagramclone.services.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.kastorcode.instagramclone.activities.MainActivity


fun goToMainActivity (activity : Activity) {
    if (FirebaseAuth.getInstance().currentUser != null) {
        val intent = Intent(activity, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
        activity.finish()
    }
}


fun goToMainActivity (context : Context, publisher : String) {
    val intent = Intent(context, MainActivity::class.java)
        .putExtra("publisher", publisher)
    context.startActivity(intent)
}