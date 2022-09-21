package com.kastorcode.instagramclone.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.kastorcode.instagramclone.AccountSettingsActivity
import com.kastorcode.instagramclone.Models.User

import com.kastorcode.instagramclone.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.view.*


class ProfileFragment : Fragment() {

    private lateinit var fragmentProfileView : View
    private lateinit var firebaseUser : FirebaseUser
    private lateinit var userFollowingRef : DatabaseReference
    private lateinit var profileId : String
    private lateinit var followersRef : DatabaseReference
    private lateinit var followingRef : DatabaseReference


    override fun onCreateView (
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View? {
        setProps(inflater, container)
        setGuiComponents()
        setClickListeners()
        return fragmentProfileView
    }


    override fun onStop () {
        super.onStop()
        setProfileIdToCurrentUser()
    }


    override fun onPause () {
        super.onPause()
        setProfileIdToCurrentUser()
    }


    override fun onDestroy () {
        super.onDestroy()
        setProfileIdToCurrentUser()
    }


    private fun setProps (inflater : LayoutInflater, container : ViewGroup?) {
        fragmentProfileView = inflater.inflate(R.layout.fragment_profile, container, false)
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        userFollowingRef = FirebaseDatabase.getInstance().reference.child("Follow")
            .child(firebaseUser.uid).child("Following")
        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        if (pref != null) {
            profileId = pref.getString("profileId", "none").toString()
            followersRef = FirebaseDatabase.getInstance().reference.child("Follow")
                .child(profileId).child("Followers")
            followingRef = FirebaseDatabase.getInstance().reference.child("Follow")
                .child(profileId).child("Following")
        }
    }


    private fun setGuiComponents () {
        fun setEditProfileBtn () {
            fun checkFollowOrFollowingButtonStatus () {
                if (userFollowingRef != null) {
                    userFollowingRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange (snapshot : DataSnapshot) {
                            if (snapshot.child(profileId).exists()) {
                                fragmentProfileView.edit_profile_btn.text = "Following"
                            }
                            else {
                                fragmentProfileView.edit_profile_btn.text = "Follow"
                            }
                        }

                        override fun onCancelled (error : DatabaseError) {
                        }
                    })
                }
            }
            if (profileId == firebaseUser?.uid) {
                fragmentProfileView.edit_profile_btn.text = "Edit Profile"
            }
            else {
                checkFollowOrFollowingButtonStatus()
            }
        }
        fun setFollowers () {
            followersRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange (snapshot : DataSnapshot) {
                    if (snapshot.exists()) {
                        fragmentProfileView.total_followers.text = snapshot.childrenCount.toString()
                    }
                }

                override fun onCancelled (error : DatabaseError) {
                }
            })
        }
        fun setFollowing () {
            followingRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange (snapshot : DataSnapshot) {
                    if (snapshot.exists()) {
                        fragmentProfileView.total_following.text = snapshot.childrenCount.toString()
                    }
                }

                override fun onCancelled (error : DatabaseError) {
                }
            })
        }
        fun setUserInfo () {
            FirebaseDatabase.getInstance().getReference().child("Users")
                .child(profileId).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange (snapshot : DataSnapshot) {
                        if (snapshot.exists()) {
                            val user = snapshot.getValue<User>(User::class.java)
                            Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile)
                                .into(fragmentProfileView.profile_image_fragment)
                            fragmentProfileView.profile_fragment_username.text = user.getUserName()
                            fragmentProfileView.full_name_profile_fragment.text = user.getFullName()
                            fragmentProfileView.bio_profile_fragment.text = user.getBio()
                        }
                    }

                    override fun onCancelled (error : DatabaseError) {
                    }
            })
        }
        setEditProfileBtn()
        setFollowers()
        setFollowing()
        setUserInfo()
    }


    private fun setClickListeners () {
        fragmentProfileView.edit_profile_btn.setOnClickListener {
            startActivity(Intent(context, AccountSettingsActivity::class.java))
        }
    }


    private fun setProfileIdToCurrentUser () {
        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", firebaseUser?.uid)
        pref?.apply()
    }
}