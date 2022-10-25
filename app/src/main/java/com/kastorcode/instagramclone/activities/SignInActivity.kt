package com.kastorcode.instagramclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.kastorcode.instagramclone.R
import com.kastorcode.instagramclone.services.auth.signIn
import com.kastorcode.instagramclone.services.navigation.goToMainActivity
import com.kastorcode.instagramclone.services.navigation.goToSignUpActivity
import kotlinx.android.synthetic.main.activity_sign_in.*
import java.lang.Exception


class SignInActivity : AppCompatActivity() {

    override fun onStart () {
        super.onStart()
        goToMainActivity(this)
    }


    override fun onCreate (savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        setClickListeners()
    }


    private fun setClickListeners () {
        signin_login_btn.setOnClickListener {
            showLoginView {
                signIn(
                    signin_email.text.toString(),
                    signin_password.text.toString(),
                    {
                        hideLoginView()
                        goToMainActivity(this)
                    },
                    { exception ->
                        hadExceptionRaised(exception)
                    }
                )
            }
        }
        signin_signup_btn.setOnClickListener {
            goToSignUpActivity(this)
        }
    }


    private fun showLoginView (callback : (() -> Unit)) {
        signin_login_view.visibility = View.VISIBLE
        callback()
    }


    private fun hideLoginView () {
        signin_login_view.visibility = View.GONE
    }


    private fun hadExceptionRaised (exception : Exception) {
        Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG).show()
        FirebaseAuth.getInstance().signOut()
        hideLoginView()
    }
}