package com.kastorcode.instagramclone.services.story

import android.app.Activity
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView


fun getStoryImage (activity : Activity) {
    CropImage.activity().setAspectRatio(9, 16)
        .setGuidelines(CropImageView.Guidelines.ON).start(activity)
}