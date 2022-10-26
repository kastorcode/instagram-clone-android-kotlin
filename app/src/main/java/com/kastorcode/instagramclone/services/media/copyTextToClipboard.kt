package com.kastorcode.instagramclone.services.media

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast


fun copyTextToClipboard (context : Context, text : String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Copied in the Instagram Clone by <kastor.code/>", text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
}