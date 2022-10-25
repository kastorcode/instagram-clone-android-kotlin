package com.kastorcode.instagramclone.services.navigation

import android.content.Context
import android.content.Intent
import com.kastorcode.instagramclone.activities.StoryActivity


fun goToStoryActivity (context : Context, userId : String) {
    val intent = Intent(context, StoryActivity::class.java)
        .putExtra("userId", userId)
    context.startActivity(intent)
}