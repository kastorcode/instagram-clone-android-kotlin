package com.kastorcode.instagramclone.Services

import android.content.Context
import android.content.Intent
import com.kastorcode.instagramclone.AddStoryActivity


fun goToAddStoryActivity (mContext : Context, userId : String) {
    val intent = Intent(mContext, AddStoryActivity::class.java)
        .putExtra("userId", userId)
    mContext.startActivity(intent)
}