package com.kastorcode.instagramclone.services.navigation

import android.app.Activity
import android.content.Intent
import com.kastorcode.instagramclone.activities.ShowUsersActivity


fun goToShowUsersActivity (activity : Activity, userId : String, storyId : String, title : String) {
    val intent = Intent(activity, ShowUsersActivity::class.java).putExtra("id", userId)
        .putExtra("storyId", storyId).putExtra("title", title)
    activity.startActivity(intent)
}