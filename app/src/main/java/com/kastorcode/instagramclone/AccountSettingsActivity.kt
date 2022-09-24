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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.kastorcode.instagramclone.Models.User
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_account_settings.*


class AccountSettingsActivity : AppCompatActivity() {

    private lateinit var firebaseUser : FirebaseUser
    private lateinit var storageRef : StorageReference
    private var userImageUri : Uri? = null


    override fun onCreate (savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)
        setProps()
        getUserInfo()
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
                profile_image_view.setImageURI(userImageUri)
            }
        }
        getUserImageUri()
    }


    private fun setProps () {
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        storageRef = FirebaseStorage.getInstance().reference.child("profile-pictures")
    }


    private fun getUserInfo () {
        FirebaseDatabase.getInstance().reference.child("Users")
            .child(firebaseUser.uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange (snapshot : DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(User::class.java)
                        Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile)
                            .into(profile_image_view)
                        profile_user_name.setText(user.getUserName())
                        profile_full_name.setText(user.getFullName())
                        profile_bio.setText(user.getBio())
                    }
                }

                override fun onCancelled (error : DatabaseError) {
                }
            })
    }


    private fun setClickListeners () {
        fun changeUserImage () {
            CropImage.activity().setAspectRatio(1, 1)
                .setGuidelines(CropImageView.Guidelines.ON).start(this)
        }
        fun signOut () {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@AccountSettingsActivity, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
        fun saveUserInfo () {
            fun uploadUserImage () {
                if (userImageUri == null) {
                    return
                }
                val fileRef = storageRef.child(firebaseUser.uid + ".jpg")
                fileRef.putFile(userImageUri!!)
                    .continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                        if (task.isSuccessful) {
                            return@Continuation fileRef.downloadUrl
                        }
                        throw task.exception!!
                    }).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val fields : Map<String, String> = mapOf(
                                "image" to task.result.toString()
                            )
                            FirebaseDatabase.getInstance().reference.child("Users")
                                .child(firebaseUser.uid).updateChildren(fields)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        hideProfileUpdatingView()
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
            fun sendUserInfo () {
                var fields : Map<String, String> = mapOf(
                    "username" to profile_user_name.text.toString().lowercase(),
                    "fullname" to profile_full_name.text.toString().lowercase(),
                    "bio" to profile_bio.text.toString()
                )
                fields = fields.filterValues { value -> value.isNotEmpty() }
                FirebaseDatabase.getInstance().reference.child("Users")
                    .child(firebaseUser.uid).updateChildren(fields).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Account has been updated successfully",
                                Toast.LENGTH_LONG).show()
                        }
                        else {
                            Toast.makeText(this, task.exception.toString(),
                                Toast.LENGTH_LONG).show()
                        }
                        if (userImageUri == null) {
                            hideProfileUpdatingView()
                        }
                    }
            }
            showProfileUpdatingView()
            uploadUserImage()
            sendUserInfo()
        }
        profile_change_image_text_btn.setOnClickListener {
            changeUserImage()
        }
        profile_logout_btn.setOnClickListener {
            signOut()
        }
        profile_save_btn.setOnClickListener {
            saveUserInfo()
        }
    }


    private fun showProfileUpdatingView () {
        profile_updating_view.visibility = View.VISIBLE
    }


    private fun hideProfileUpdatingView () {
        profile_updating_view.visibility = View.INVISIBLE
    }


    private fun hadExceptionRaised (message : String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        hideProfileUpdatingView()
    }
}