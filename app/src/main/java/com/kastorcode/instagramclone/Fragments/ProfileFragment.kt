package com.kastorcode.instagramclone.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.kastorcode.instagramclone.AccountSettingsActivity
import com.kastorcode.instagramclone.Adapters.MyImagesAdapter
import com.kastorcode.instagramclone.Models.Post
import com.kastorcode.instagramclone.Models.User
import com.kastorcode.instagramclone.R
import com.kastorcode.instagramclone.Services.followUser
import com.kastorcode.instagramclone.Services.unfollowUser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.util.*
import kotlin.collections.ArrayList


class ProfileFragment : Fragment() {

    private lateinit var fragmentProfileView : View
    private lateinit var firebaseUser : FirebaseUser
    private lateinit var userFollowingRef : DatabaseReference
    private lateinit var profileId : String
    private lateinit var followersRef : DatabaseReference
    private lateinit var followingRef : DatabaseReference
    private lateinit var profileUploadedImages : RecyclerView
    private lateinit var postList : MutableList<Post>
    private lateinit var myImagesAdapter : MyImagesAdapter


    override fun onCreateView (
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        setProps(inflater, container)
        setGuiComponents()
        getUploadedImages()
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
        postList = ArrayList()
        myImagesAdapter = context?.let { MyImagesAdapter(it, postList) }!!
        val gridLayoutManager = GridLayoutManager(context, 3)
        profileUploadedImages = fragmentProfileView.findViewById(R.id.profile_uploaded_images)
        profileUploadedImages.setHasFixedSize(true)
        profileUploadedImages.layoutManager = gridLayoutManager
        profileUploadedImages.adapter = myImagesAdapter
    }


    private fun setGuiComponents () {
        fun setEditProfileBtn () {
            fun checkFollowOrFollowingButtonStatus () {
                if (userFollowingRef != null) {
                    userFollowingRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange (snapshot : DataSnapshot) {
                            if (snapshot.child(profileId).exists()) {
                                fragmentProfileView.profile_edit_btn.text = "Following"
                            }
                            else {
                                fragmentProfileView.profile_edit_btn.text = "Follow"
                            }
                        }

                        override fun onCancelled (error : DatabaseError) {
                        }
                    })
                }
            }
            if (profileId == firebaseUser.uid) {
                fragmentProfileView.profile_edit_btn.text = "Edit Profile"
            }
            else {
                checkFollowOrFollowingButtonStatus()
            }
        }
        fun setFollowers () {
            followersRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange (snapshot : DataSnapshot) {
                    if (snapshot.exists()) {
                        fragmentProfileView.profile_total_followers.text = snapshot.childrenCount.toString()
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
                        fragmentProfileView.profile_total_following.text = snapshot.childrenCount.toString()
                    }
                }

                override fun onCancelled (error : DatabaseError) {
                }
            })
        }
        fun setUserInfo () {
            FirebaseDatabase.getInstance().reference.child("Users")
                .child(profileId).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange (snapshot : DataSnapshot) {
                        if (snapshot.exists()) {
                            val user = snapshot.getValue(User::class.java)
                            Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile)
                                .into(fragmentProfileView.profile_image)
                            fragmentProfileView.profile_username.text = user.getUserName()
                            fragmentProfileView.profile_fullname.text = user.getFullName()
                            fragmentProfileView.profile_bio.text = user.getBio()
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


    private fun getUploadedImages () {
        FirebaseDatabase.getInstance().reference.child("Posts")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange (dataSnapshot : DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        postList.clear()
                        for (snapshot in dataSnapshot.children) {
                            val post = snapshot.getValue(Post::class.java)
                            if (post?.getPublisher() == profileId) {
                                postList.add(post)
                            }
                        }
                        postList.reverse()
                        myImagesAdapter.notifyDataSetChanged()
                    }
                }

                override fun onCancelled (error : DatabaseError) {
                }
            })
    }


    private fun setClickListeners () {
        fun handleEditProfileBtnClick () {
            when (fragmentProfileView.profile_edit_btn.text) {
                "Edit Profile" -> startActivity(Intent(
                    context, AccountSettingsActivity::class.java))
                "Follow" -> followUser(profileId)
                "Following" -> unfollowUser(profileId)
            }
        }
        fragmentProfileView.profile_edit_btn.setOnClickListener {
            handleEditProfileBtnClick()
        }
    }


    private fun setProfileIdToCurrentUser () {
        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", firebaseUser.uid)
        pref?.apply()
    }
}