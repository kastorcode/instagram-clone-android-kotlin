package com.kastorcode.instagramclone.services.navigation

import android.app.Activity


fun goBack (activity : Activity) {
    activity.onBackPressed()
}