package com.kastorcode.instagramclone.services.navigation

import android.content.Context
import android.content.Intent
import com.kastorcode.instagramclone.activities.PostCommentsActivity


fun goToPostCommentsActivity (context : Context, publisher : String, postId : String) {
    val intent = Intent(context, PostCommentsActivity::class.java)
        .putExtra("publisher", publisher).putExtra("postId", postId)
    context.startActivity(intent)
}