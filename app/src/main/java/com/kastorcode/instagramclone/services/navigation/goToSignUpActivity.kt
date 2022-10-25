package com.kastorcode.instagramclone.services.navigation

import android.app.Activity
import android.content.Intent
import com.kastorcode.instagramclone.activities.SignUpActivity


fun goToSignUpActivity (activity : Activity) {
    activity.startActivity(Intent(activity, SignUpActivity::class.java))
}