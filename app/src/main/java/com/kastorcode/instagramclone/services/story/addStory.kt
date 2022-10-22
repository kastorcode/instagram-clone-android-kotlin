package com.kastorcode.instagramclone.services.story

import android.net.Uri


fun addStory (
    storyImageUri : Uri,
    callback : (() -> Unit)? = null, errorCallback : ((exception : Exception) -> Unit)? = null
) {
    uploadStoryImage(
        storyImageUri,
        { imageUrl ->
            createStory(imageUrl, callback, errorCallback)
        },
        errorCallback
    )
}