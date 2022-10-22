package com.kastorcode.instagramclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.kastorcode.instagramclone.R
import com.kastorcode.instagramclone.services.user.deleteUserAccount
import com.kastorcode.instagramclone.services.user.signOut
import kotlinx.android.synthetic.main.activity_delete_account.*
import java.lang.Exception


class DeleteAccountActivity : AppCompatActivity() {

    override fun onCreate (savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_account)
        setClickListeners()
    }


    private fun setClickListeners () {
        delete_account_cancel_btn.setOnClickListener {
            finish()
        }
        delete_account_btn.setOnClickListener {
            showDeleteAccountLoadingView {
                deleteUserAccount(
                    delete_account_email.text.toString(),
                    delete_account_password.text.toString(),
                    { signOut(this) },
                    { exception -> hadExceptionRaised(exception) }
                )
            }
        }
    }


    private fun showDeleteAccountLoadingView (callback : (() -> Unit)) {
        delete_account_loading_view.visibility = View.VISIBLE
        callback()
    }


    private fun hideDeleteAccountLoadingView () {
        delete_account_loading_view.visibility = View.GONE
    }


    private fun hadExceptionRaised (exception : Exception) {
        Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG).show()
        hideDeleteAccountLoadingView()
    }
}