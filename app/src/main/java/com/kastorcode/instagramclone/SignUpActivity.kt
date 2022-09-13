package com.kastorcode.instagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity() {

    override fun onCreate (savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setClickListeners()
    }


    private fun setClickListeners () {
        signup_register_btn.setOnClickListener {
            createAccount()
        }
        signup_signin_btn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }


    private fun createAccount () {
        fun getFields () : MutableMap<String, String> {
            return mutableMapOf(
                "fullname" to signup_fullname.text.toString(),
                "username" to signup_username.text.toString(),
                "email" to signup_email.text.toString(),
                "password" to signup_password.text.toString()
            )
        }
        fun isFieldsValid (fields : MutableMap<String, String>) : Boolean {
            fun showMessage (field : String) {
                Toast.makeText(this, "$field is required", Toast.LENGTH_LONG).show()
            }
            when {
                TextUtils.isEmpty(fields["fullname"]) -> showMessage("Full name")
                TextUtils.isEmpty(fields["username"]) -> showMessage("User name")
                TextUtils.isEmpty(fields["email"]) -> showMessage("Email")
                TextUtils.isEmpty(fields["password"]) -> showMessage("Password")
                else -> {
                    return true
                }
            }
            return false
        }
        fun saveUserToDatabase (fields : MutableMap<String, String>, auth : FirebaseAuth) {
            fun userSaved () {
                hideRegisteringView()
                Toast.makeText(this, "Account has been created successfully", Toast.LENGTH_LONG).show()
                val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            fields.remove("password")
            fields["uid"] = auth.currentUser!!.uid
            fields["bio"] = "Hey, I am using an Instagram Clone by <kastor.code/>"
            fields["image"] = "https://firebasestorage.googleapis.com/v0/b/instagram-clone-39239.appspot.com/o/default-images%2Fprofile.png?alt=media"
            FirebaseDatabase.getInstance().reference.child("Users")
                .child(fields["uid"]!!).setValue(fields)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        userSaved()
                    }
                    else {
                        hadExceptionRaised(task.exception.toString())
                    }
                }
        }
        fun createUser (fields : MutableMap<String, String>) {
            val auth = FirebaseAuth.getInstance()
            auth.createUserWithEmailAndPassword(fields["email"]!!, fields["password"]!!)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        saveUserToDatabase(fields, auth)
                    }
                    else {
                        hadExceptionRaised(task.exception.toString())
                    }
                }
        }
        val fields = getFields()
        if (!isFieldsValid(fields)) return
        showRegisteringView()
        createUser(fields)
    }


    private fun hadExceptionRaised (message : String) {
        Toast.makeText(this, "[Error] $message", Toast.LENGTH_LONG).show()
        FirebaseAuth.getInstance().signOut()
        hideRegisteringView()
    }


    private fun showRegisteringView () {
        signup_registering_view.visibility = View.VISIBLE
    }


    private fun hideRegisteringView () {
        signup_registering_view.visibility = View.INVISIBLE
    }
}