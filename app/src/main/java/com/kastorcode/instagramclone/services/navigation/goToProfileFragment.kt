package com.kastorcode.instagramclone.services.navigation

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.kastorcode.instagramclone.Fragments.ProfileFragment
import com.kastorcode.instagramclone.R


fun goToProfileFragment (mContext : Context, profileId : String) {
    mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
        .putString("profileId", profileId).apply()
    (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
        .replace(R.id.fragment_container, ProfileFragment()).commit()
}