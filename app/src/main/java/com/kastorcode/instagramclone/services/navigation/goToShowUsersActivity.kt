package com.kastorcode.instagramclone.services.navigation

import android.content.Context
import android.content.Intent
import com.kastorcode.instagramclone.activities.ShowUsersActivity


fun goToShowUsersActivity (context : Context, userId : String, storyId : String, title : String) {
    val intent = Intent(context, ShowUsersActivity::class.java).putExtra("id", userId)
        .putExtra("storyId", storyId).putExtra("title", title)
    context.startActivity(intent)
}