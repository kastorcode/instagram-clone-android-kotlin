package com.kastorcode.instagramclone.services.post

import android.app.Activity
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView


fun getPostImage (activity : Activity) {
    CropImage.activity().setAspectRatio(2, 1)
        .setGuidelines(CropImageView.Guidelines.ON).start(activity)
}