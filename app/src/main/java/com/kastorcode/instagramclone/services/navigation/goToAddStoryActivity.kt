package com.kastorcode.instagramclone.services.navigation

import android.content.Context
import android.content.Intent
import com.kastorcode.instagramclone.activities.AddStoryActivity


fun goToAddStoryActivity (mContext : Context, userId : String) {
    val intent = Intent(mContext, AddStoryActivity::class.java)
        .putExtra("userId", userId)
    mContext.startActivity(intent)
}