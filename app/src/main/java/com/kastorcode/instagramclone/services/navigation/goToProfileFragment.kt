package com.kastorcode.instagramclone.services.navigation

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.kastorcode.instagramclone.fragments.ProfileFragment
import com.kastorcode.instagramclone.R


fun goToProfileFragment (context : Context, profileId : String) {
    context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
        .putString("profileId", profileId).apply()
    (context as FragmentActivity).supportFragmentManager.beginTransaction()
        .replace(R.id.fragment_container, ProfileFragment()).commit()
}