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
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_add_story.*


class AddStoryActivity : AppCompatActivity() {

    private lateinit var firebaseUserUid : String
    private lateinit var storageRef : StorageReference
    private lateinit var storyImageUri : Uri


    override fun onCreate (savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_story)
        setProps()
        getImage()
    }


    private fun setProps () {
        firebaseUserUid = FirebaseAuth.getInstance().currentUser!!.uid
        storageRef = FirebaseStorage.getInstance().reference.child("stories-pictures")
    }


    private fun getImage () {
        CropImage.activity().setAspectRatio(9, 16)
            .setGuidelines(CropImageView.Guidelines.ON).start(this)
    }


    override fun onActivityResult (requestCode : Int, resultCode : Int, data : Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fun uploadStory () {
            val fileRef = storageRef.child(System.currentTimeMillis().toString()
                    + ".jpg")
            fileRef.putFile(storyImageUri)
                .continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (task.isSuccessful) {
                        return@Continuation fileRef.downloadUrl
                    }
                    throw task.exception!!
                }).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val storiesRef = FirebaseDatabase.getInstance().reference
                            .child("Stories").child(firebaseUserUid)
                        val fields : Map<String, Any> = mapOf(
                            "userId" to firebaseUserUid,
                            "storyId" to storiesRef.push().key!!,
                            "imageUrl" to task.result.toString(),
                            "timeStart" to ServerValue.TIMESTAMP,
                            "timeEnd" to System.currentTimeMillis() + 86400000 // 1 day
                        )
                        storiesRef.child(fields["storyId"].toString()).updateChildren(fields)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "Story published successfully",
                                        Toast.LENGTH_LONG).show()
                                    hideAddStoryAddingView()
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
        fun getStoryImageUri () {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE &&
                resultCode == Activity.RESULT_OK &&
                data != null
            ) {
                showAddStoryAddingView()
                storyImageUri = CropImage.getActivityResult(data).uri
                uploadStory()
            }
        }
        getStoryImageUri()
    }


    private fun showAddStoryAddingView () {
        add_story_adding_view.visibility = View.VISIBLE
    }


    private fun hideAddStoryAddingView () {
        add_story_adding_view.visibility = View.INVISIBLE
    }


    private fun hadExceptionRaised (message : String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        hideAddStoryAddingView()
    }
}