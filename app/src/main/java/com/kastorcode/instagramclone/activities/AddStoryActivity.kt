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
import com.kastorcode.instagramclone.services.story.addStory
import com.kastorcode.instagramclone.services.story.getStoryImage
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_add_story.*
import java.lang.Exception


class AddStoryActivity : AppCompatActivity() {

    private lateinit var firebaseUserUid : String
    private lateinit var storyImageUri : Uri


    override fun onCreate (savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_story)
        setProps()
        getStoryImage(this)
    }


    private fun setProps () {
        firebaseUserUid = FirebaseAuth.getInstance().currentUser!!.uid
    }


    override fun onActivityResult (requestCode : Int, resultCode : Int, data : Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fun getStoryImageUri () {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE &&
                resultCode == Activity.RESULT_OK &&
                data != null
            ) {
                showAddStoryAddingView {
                    storyImageUri = CropImage.getActivityResult(data).uri
                    addStory(
                        storyImageUri,
                        {
                            Toast.makeText(this, "Story published successfully",
                                Toast.LENGTH_LONG).show()
                            hideAddStoryAddingView()
                            finish()
                        }
                    ) { exception ->
                        hadExceptionRaised(exception)
                    }
                }
            }
            else {
                finish()
            }
        }
        getStoryImageUri()
    }


    private fun showAddStoryAddingView (callback : (() -> Unit)) {
        add_story_adding_view.visibility = View.VISIBLE
        callback()
    }


    private fun hideAddStoryAddingView () {
        add_story_adding_view.visibility = View.GONE
    }


    private fun hadExceptionRaised (exception : Exception) {
        Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG).show()
        hideAddStoryAddingView()
        finish()
    }
}