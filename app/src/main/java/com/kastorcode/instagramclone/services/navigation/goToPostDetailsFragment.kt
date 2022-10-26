package com.kastorcode.instagramclone.services.navigation

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.kastorcode.instagramclone.fragments.PostDetailsFragment
import com.kastorcode.instagramclone.R


fun goToPostDetailsFragment (mContext : Context, postId : String) {
    mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
        .putString("postId", postId).apply()
    (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
        .replace(R.id.fragment_container, PostDetailsFragment()).commit()
}