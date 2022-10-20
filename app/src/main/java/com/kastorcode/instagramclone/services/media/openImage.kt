package com.kastorcode.instagramclone.services.media

import android.app.Activity
import android.content.Intent
import android.net.Uri


fun openImage (activity : Activity, uri : String) {
    activity.startActivity(
        Intent().setAction(Intent.ACTION_VIEW).setDataAndType(Uri.parse(uri), "image/*")
    )
}