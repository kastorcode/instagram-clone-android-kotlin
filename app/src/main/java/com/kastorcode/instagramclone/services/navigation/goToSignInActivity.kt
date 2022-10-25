package com.kastorcode.instagramclone.services.navigation

import android.app.Activity
import android.content.Intent
import com.kastorcode.instagramclone.activities.SignInActivity


fun goToSignInActivity (activity : Activity) {
    activity.startActivity(Intent(activity, SignInActivity::class.java))
}