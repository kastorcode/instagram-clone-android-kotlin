package com.kastorcode.instagramclone.services.post

import android.net.Uri


fun addPost (
    postImageUri : Uri, description : String,
    callback : (() -> Unit)? = null, errorCallback : ((exception : Exception) -> Unit)? = null
) {
    uploadPostImage(
        postImageUri,
        { postImage ->
            createPost(postImage, description, callback, errorCallback)
        },
        errorCallback
    )
}