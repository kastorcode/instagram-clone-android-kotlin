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
import com.kastorcode.instagramclone.services.post.addPost
import com.kastorcode.instagramclone.services.post.pickPostImage
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_add_post.*
import java.lang.Exception


class AddPostActivity : AppCompatActivity() {

    private lateinit var firebaseUserUid : String
    private var postImageUri : Uri? = null


    override fun onCreate (savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
        setProps()
        setClickListeners()
        pickPostImage(this)
    }


    override fun onActivityResult (requestCode : Int, resultCode : Int, data : Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fun getPostImageUri () {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE &&
                resultCode == Activity.RESULT_OK &&
                data != null
            ) {
                postImageUri = CropImage.getActivityResult(data).uri
                add_post_image_view.setImageURI(postImageUri)
            }
        }
        getPostImageUri()
    }


    private fun setProps () {
        firebaseUserUid = FirebaseAuth.getInstance().currentUser!!.uid
    }


    private fun setClickListeners () {
        add_post_close_btn.setOnClickListener {
            finish()
        }
        add_post_save_btn.setOnClickListener {
            if (postImageUri != null) {
                showAddPostAddingView {
                    addPost(
                        postImageUri!!,
                        add_post_description.text.toString(),
                        {
                            Toast.makeText(this, "Post published successfully",
                                Toast.LENGTH_LONG).show()
                            hideAddPostAddingView()
                        },
                        { exception ->
                            hadExceptionRaised(exception)
                        }
                    )
                }
            }
        }
        add_post_image_view.setOnClickListener {
            pickPostImage(this)
        }
    }


    private fun showAddPostAddingView (callback : (() -> Unit)) {
        add_post_adding_view.visibility = View.VISIBLE
        callback()
    }


    private fun hideAddPostAddingView () {
        add_post_adding_view.visibility = View.GONE
    }


    private fun hadExceptionRaised (exception : Exception) {
        Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG).show()
        hideAddPostAddingView()
    }
}