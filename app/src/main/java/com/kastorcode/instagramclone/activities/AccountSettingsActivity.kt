package com.kastorcode.instagramclone.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.kastorcode.instagramclone.R
import com.kastorcode.instagramclone.services.media.openImage
import com.kastorcode.instagramclone.services.navigation.goToDeleteAccountActivity
import com.kastorcode.instagramclone.services.user.*
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import java.lang.Exception
import kotlinx.android.synthetic.main.activity_account_settings.*


class AccountSettingsActivity : AppCompatActivity() {

    private lateinit var firebaseUserUid : String
    private lateinit var userProfileImage : String
    private var userImageUri : Uri? = null


    override fun onCreate (savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)
        setProps()
        getUser(firebaseUserUid) { user ->
            userProfileImage = user.getImage()
            Picasso.get().load(userProfileImage).placeholder(R.drawable.profile)
                .into(account_profile_image_view)
            account_username.setText(user.getUserName())
            account_fullname.setText(user.getFullName())
            account_bio.setText(user.getBio())
        }
        setClickListeners()
    }


    override fun onActivityResult (requestCode : Int, resultCode : Int, data : Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fun getUserImageUri () {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE &&
                resultCode == Activity.RESULT_OK &&
                data != null
            ) {
                userImageUri = CropImage.getActivityResult(data).uri
                account_profile_image_view.setImageURI(userImageUri)
            }
        }
        getUserImageUri()
    }


    private fun setProps () {
        firebaseUserUid = FirebaseAuth.getInstance().currentUser!!.uid
    }


    private fun setClickListeners () {
        account_close_btn.setOnClickListener {
            finish()
        }
        account_save_btn.setOnClickListener {
            showAccountLoadingView {
                updateUserProfileImage(
                    userImageUri,
                    {
                        if (userImageUri != null) {
                            hideAccountLoadingView()
                        }
                    },
                    { exception -> hadExceptionRaised(exception) }
                )
                updateUserInfo(
                    this,
                    account_username.text.toString().lowercase(),
                    account_fullname.text.toString().lowercase(),
                    account_bio.text.toString(),
                    {
                        if (userImageUri == null) {
                            hideAccountLoadingView()
                        }
                    },
                    { exception -> hadExceptionRaised(exception) }
                )
            }
        }
        account_profile_image_view.setOnClickListener {
            openImage(this, userProfileImage)
        }
        account_change_profile_image_txt.setOnClickListener {
            setUserProfileImage(this)
        }
        account_logout_btn.setOnClickListener {
            signOut(this)
        }
        account_delete_btn.setOnClickListener {
            goToDeleteAccountActivity(this)
        }
    }


    private fun showAccountLoadingView (callback : (() -> Unit)) {
        account_loading_view.visibility = View.VISIBLE
        callback()
    }


    private fun hideAccountLoadingView () {
        account_loading_view.visibility = View.GONE
    }


    private fun hadExceptionRaised (exception : Exception) {
        Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG).show()
        hideAccountLoadingView()
    }
}