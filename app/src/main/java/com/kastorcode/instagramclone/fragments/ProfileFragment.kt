package com.kastorcode.instagramclone.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kastorcode.instagramclone.adapters.MyImagesAdapter
import com.kastorcode.instagramclone.models.Post
import com.kastorcode.instagramclone.models.User
import com.kastorcode.instagramclone.R
import com.kastorcode.instagramclone.services.media.copyTextToClipboard
import com.kastorcode.instagramclone.services.media.openImage
import com.kastorcode.instagramclone.services.navigation.goToAccountSettingsActivity
import com.kastorcode.instagramclone.services.navigation.goToShowUsersActivity
import com.kastorcode.instagramclone.services.post.getUserPosts
import com.kastorcode.instagramclone.services.post.getUserPostsCount
import com.kastorcode.instagramclone.services.post.getUserSavedPosts
import com.kastorcode.instagramclone.services.user.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlin.collections.ArrayList


class ProfileFragment : Fragment() {

    private lateinit var fragmentProfileView : View
    private lateinit var firebaseUserUid : String
    private lateinit var userFollowingRef : DatabaseReference
    private lateinit var profileId : String
    private lateinit var user : User
    private lateinit var profileUploadedImages : RecyclerView
    private lateinit var postList : MutableList<Post>
    private lateinit var myImagesAdapter : MyImagesAdapter
    private lateinit var profileSavedImages : RecyclerView
    private lateinit var mySavedImages : MutableList<String>
    private lateinit var savedPostList : MutableList<Post>
    private lateinit var mySavedImagesAdapter : MyImagesAdapter


    override fun onCreateView (
        inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?
    ) : View {
        setProps(inflater, container)
        setGuiComponents()
        getUserPosts(profileId, postList, myImagesAdapter)
        getUserSavedPosts(profileId, mySavedImages, savedPostList, mySavedImagesAdapter)
        setClickListeners()
        showUploadedImages()
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
        fun setSomeUserProps () {
            val pref = context!!.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
            profileId = pref.getString("profileId", "none").toString()
            fragmentProfileView = inflater.inflate(R.layout.fragment_profile, container, false)
            firebaseUserUid = FirebaseAuth.getInstance().currentUser!!.uid
            userFollowingRef = FirebaseDatabase.getInstance().reference.child("Follow")
                .child(firebaseUserUid).child("Following")
        }
        fun setUploadedImages () {
            postList = ArrayList()
            myImagesAdapter = MyImagesAdapter(context!!, postList)
            val uploadedImagesLayoutManager = GridLayoutManager(context, 3)
            profileUploadedImages = fragmentProfileView.findViewById(R.id.profile_uploaded_images)
            profileUploadedImages.setHasFixedSize(true)
            profileUploadedImages.layoutManager = uploadedImagesLayoutManager
            profileUploadedImages.adapter = myImagesAdapter
        }
        fun setSavedImages () {
            mySavedImages = ArrayList()
            savedPostList = ArrayList()
            mySavedImagesAdapter = MyImagesAdapter(context!!, savedPostList)
            val savedImagesLayoutManager = GridLayoutManager(context, 3)
            profileSavedImages = fragmentProfileView.findViewById(R.id.profile_saved_images)
            profileSavedImages.setHasFixedSize(true)
            profileSavedImages.layoutManager = savedImagesLayoutManager
            profileSavedImages.adapter = mySavedImagesAdapter
        }
        setSomeUserProps()
        setUploadedImages()
        setSavedImages()
    }


    private fun setGuiComponents () {
        fun setEditProfileBtn () {
            if (profileId == firebaseUserUid) {
                fragmentProfileView.profile_edit_btn.text = "Edit Profile"
            }
            else {
                userIsFollowing(userFollowingRef, profileId) { isFollowing ->
                    if (isFollowing) {
                        fragmentProfileView.profile_edit_btn.text = "Following"
                    }
                    else {
                        fragmentProfileView.profile_edit_btn.text = "Follow"
                    }
                }
            }
        }
        getUser(profileId) {
            user = it
            Picasso.get().load(user.getImage()).placeholder(R.drawable.profile)
                .into(fragmentProfileView.profile_image)
            fragmentProfileView.profile_username.text = user.getUserName()
            fragmentProfileView.profile_fullname.text = user.getFullName()
            fragmentProfileView.profile_bio.text = user.getBio()
        }
        setEditProfileBtn()
        getUserPostsCount(profileId) { count ->
            fragmentProfileView.profile_total_posts.text = "$count"
        }
        getUserFollowersCount(profileId) { count ->
            fragmentProfileView.profile_total_followers.text = "$count"
        }
        getUserFollowingCount(profileId) { count ->
            fragmentProfileView.profile_total_following.text = "$count"
        }
    }


    private fun setClickListeners () {
        fragmentProfileView.profile_image.setOnClickListener {
            openImage(context!!, user.getImage())
        }
        fragmentProfileView.profile_followers_view.setOnClickListener {
            goToShowUsersActivity(context!!, profileId, "", "Followers")
        }
        fragmentProfileView.profile_following_view.setOnClickListener {
            goToShowUsersActivity(context!!, profileId, "", "Following")
        }
        fragmentProfileView.profile_edit_btn.setOnClickListener {
            if (fragmentProfileView.profile_edit_btn.text == "Edit Profile") {
                goToAccountSettingsActivity(context!!)
            }
            else {
                followUser(profileId, fragmentProfileView.profile_edit_btn.text.toString())
            }
        }
        fragmentProfileView.profile_bio.setOnClickListener {
            copyTextToClipboard(context!!, user.getBio())
        }
        fragmentProfileView.profile_uploaded_images_btn.setOnClickListener {
            showUploadedImages()
        }
        fragmentProfileView.profile_saved_images_btn.setOnClickListener {
            showSavedImages()
        }
    }


    private fun showUploadedImages () {
        fragmentProfileView.profile_saved_images.visibility = View.GONE
        fragmentProfileView.profile_uploaded_images.visibility = View.VISIBLE
    }


    private fun showSavedImages () {
        fragmentProfileView.profile_uploaded_images.visibility = View.GONE
        fragmentProfileView.profile_saved_images.visibility = View.VISIBLE
    }


    private fun setProfileIdToCurrentUser () {
        val pref = context!!.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
        pref.putString("profileId", firebaseUserUid)
        pref.apply()
    }
}