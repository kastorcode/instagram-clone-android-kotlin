package com.kastorcode.instagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignInActivity : AppCompatActivity() {

    override fun onStart () {
        super.onStart()
        redirectIfLogged()
    }


    override fun onCreate (savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        setClickListeners()
    }


    private fun redirectIfLogged () {
        if (FirebaseAuth.getInstance().currentUser != null) {
            val intent = Intent(this@SignInActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }


    private fun setClickListeners () {
        signin_login_btn.setOnClickListener {
            signIn()
        }
        signin_signup_btn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }


    private fun signIn () {
        fun getFields () : Map<String, String> {
            return mapOf(
                "email" to signin_email.text.toString(),
                "password" to signin_password.text.toString()
            )
        }
        fun isFieldsValid (fields : Map<String, String>) : Boolean {
            fun showMessage (field : String) {
                Toast.makeText(this, "$field is required", Toast.LENGTH_LONG).show()
            }
            when {
                TextUtils.isEmpty(fields["email"]) -> showMessage("Email")
                TextUtils.isEmpty(fields["password"]) -> showMessage("Password")
                else -> {
                    return true
                }
            }
            return false
        }
        fun loginUser (fields : Map<String, String>) {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(fields["email"]!!, fields["password"]!!)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        hideLoginView()
                        redirectIfLogged()
                    }
                    else {
                        hadExceptionRaised(task.exception.toString())
                    }
                }
        }
        val fields = getFields()
        if (!isFieldsValid(fields)) return
        showLoginView()
        loginUser(fields)
    }


    private fun hadExceptionRaised (message : String) {
        Toast.makeText(this, "[Error] $message", Toast.LENGTH_LONG).show()
        FirebaseAuth.getInstance().signOut()
        hideLoginView()
    }


    private fun showLoginView () {
        signin_login_view.visibility = View.VISIBLE
    }


    private fun hideLoginView () {
        signin_login_view.visibility = View.GONE
    }
}