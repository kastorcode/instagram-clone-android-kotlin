package com.kastorcode.instagramclone.services.navigation

import android.content.Context
import android.content.Intent
import com.kastorcode.instagramclone.activities.AccountSettingsActivity


fun goToAccountSettingsActivity (context : Context) {
    context.startActivity(Intent(context, AccountSettingsActivity::class.java))
}