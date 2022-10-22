package com.kastorcode.instagramclone.services.navigation

import android.app.Activity
import android.content.Intent
import com.kastorcode.instagramclone.activities.DeleteAccountActivity


fun goToDeleteAccountActivity (activity : Activity) {
    activity.startActivity(Intent(activity, DeleteAccountActivity::class.java))
}