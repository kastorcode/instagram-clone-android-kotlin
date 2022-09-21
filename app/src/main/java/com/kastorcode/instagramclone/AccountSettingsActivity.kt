package com.kastorcode.instagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.Models.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_account_settings.*


class AccountSettingsActivity : AppCompatActivity() {

    private lateinit var firebaseUser : FirebaseUser


    override fun onCreate (savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)
        setProps()
        getUserInfo()
        setClickListeners()
    }


    private fun setProps () {
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
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
        fun signOut () {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@AccountSettingsActivity, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
        fun saveUserInfo () {
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
                }
        }
        profile_logout_btn.setOnClickListener {
            signOut()
        }
        profile_save_btn.setOnClickListener {
            saveUserInfo()
        }
    }
}