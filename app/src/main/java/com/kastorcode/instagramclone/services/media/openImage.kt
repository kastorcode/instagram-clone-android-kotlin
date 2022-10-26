package com.kastorcode.instagramclone.services.media

import android.content.Context
import android.content.Intent
import android.net.Uri


fun openImage (context : Context, uri : String) {
    context.startActivity(
        Intent().setAction(Intent.ACTION_VIEW).setDataAndType(Uri.parse(uri), "image/*")
    )
}