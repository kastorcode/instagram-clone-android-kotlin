package com.kastorcode.instagramclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.kastorcode.instagramclone.R
import com.kastorcode.instagramclone.services.auth.signUp
import com.kastorcode.instagramclone.services.navigation.goToMainActivity
import com.kastorcode.instagramclone.services.navigation.goToSignInActivity
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.lang.Exception


class SignUpActivity : AppCompatActivity() {

    override fun onCreate (savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setClickListeners()
    }


    private fun setClickListeners () {
        signup_register_btn.setOnClickListener {
            showRegisteringView {
                signUp(
                    signup_fullname.text.toString().lowercase(),
                    signup_username.text.toString().lowercase(),
                    signup_email.text.toString(),
                    signup_password.text.toString(),
                    {
                        hideRegisteringView()
                        Toast.makeText(this, "Account has been created successfully",
                            Toast.LENGTH_LONG).show()
                        goToMainActivity(this)
                    },
                    { exception ->
                        hadExceptionRaised(exception)
                    }
                )
            }
        }
        signup_signin_btn.setOnClickListener {
            goToSignInActivity(this)
        }
    }


    private fun showRegisteringView (callback : (() -> Unit)) {
        signup_registering_view.visibility = View.VISIBLE
        callback()
    }


    private fun hideRegisteringView () {
        signup_registering_view.visibility = View.GONE
    }


    private fun hadExceptionRaised (exception : Exception) {
        Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG).show()
        FirebaseAuth.getInstance().signOut()
        hideRegisteringView()
    }
}