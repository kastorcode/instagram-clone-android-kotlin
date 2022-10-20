package com.kastorcode.instagramclone.services.user

import android.app.Activity
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView


fun setUserProfileImage (activity : Activity) {
    CropImage.activity().setAspectRatio(1, 1)
        .setGuidelines(CropImageView.Guidelines.ON).start(activity)
}