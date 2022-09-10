package com.kastorcode.instagramclone.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kastorcode.instagramclone.AccountSettingsActivity

import com.kastorcode.instagramclone.R
import kotlinx.android.synthetic.main.fragment_profile.view.*


class ProfileFragment : Fragment() {

    override fun onCreateView (
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        view.edit_profile_btn.setOnClickListener {
            startActivity(Intent(context, AccountSettingsActivity::class.java))
        }
        return view
    }

}