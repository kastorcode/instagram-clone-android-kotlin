package com.kastorcode.instagramclone

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_add_post.*


class AddPostActivity : AppCompatActivity() {

    private lateinit var firebaseUser : FirebaseUser
    private lateinit var storageRef : StorageReference
    private var postImageUri : Uri? = null


    override fun onCreate (savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
        setProps()
        setClickListeners()
        getImage()
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
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        storageRef = FirebaseStorage.getInstance().reference.child("posts-pictures")
    }


    private fun setClickListeners () {
        fun savePost () {
            if (postImageUri == null) {
                return
            }
            fun uploadPostImage () {
                val fileRef = storageRef.child(System.currentTimeMillis().toString()
                        + ".jpg")
                fileRef.putFile(postImageUri!!)
                    .continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                        if (task.isSuccessful) {
                            return@Continuation fileRef.downloadUrl
                        }
                        throw task.exception!!
                    }).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val postsRef = FirebaseDatabase.getInstance().reference.child("Posts")
                            val fields : Map<String, String> = mapOf(
                                "description" to add_post_description.text.toString(),
                                "postId" to postsRef.push().key!!,
                                "postImage" to task.result.toString(),
                                "publisher" to firebaseUser.uid
                            )
                            postsRef.child(fields["postId"]!!).updateChildren(fields)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(this, "Post published successfully",
                                            Toast.LENGTH_LONG).show()
                                        hideAddPostAddingView()
                                    }
                                    else {
                                        hadExceptionRaised(task.exception.toString())
                                    }
                                }
                        }
                        else {
                            hadExceptionRaised(task.exception.toString())
                        }
                    }
            }
            showAddPostAddingView()
            uploadPostImage()
        }
        add_post_save_btn.setOnClickListener {
            savePost()
        }
    }


    private fun getImage () {
        CropImage.activity().setAspectRatio(2, 1)
            .setGuidelines(CropImageView.Guidelines.ON).start(this)
    }


    private fun showAddPostAddingView () {
        add_post_adding_view.visibility = View.VISIBLE
    }


    private fun hideAddPostAddingView () {
        add_post_adding_view.visibility = View.INVISIBLE
    }


    private fun hadExceptionRaised (message : String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        hideAddPostAddingView()
    }
}