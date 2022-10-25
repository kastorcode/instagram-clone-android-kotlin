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
import com.kastorcode.instagramclone.activities.AccountSettingsActivity
import com.kastorcode.instagramclone.adapters.MyImagesAdapter
import com.kastorcode.instagramclone.Models.Post
import com.kastorcode.instagramclone.Models.User
import com.kastorcode.instagramclone.R
import com.kastorcode.instagramclone.services.user.followUser
import com.kastorcode.instagramclone.services.unfollowUser
import com.kastorcode.instagramclone.activities.ShowUsersActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlin.collections.ArrayList


class ProfileFragment : Fragment() {

    private lateinit var fragmentProfileView : View
    private lateinit var firebaseUser : FirebaseUser
    private lateinit var userFollowingRef : DatabaseReference
    private lateinit var profileId : String
    private lateinit var postsRef : DatabaseReference
    private lateinit var followersRef : DatabaseReference
    private lateinit var followingRef : DatabaseReference
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
        getUploadedImages()
        getSavedImages()
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
            fragmentProfileView = inflater.inflate(R.layout.fragment_profile, container, false)
            firebaseUser = FirebaseAuth.getInstance().currentUser!!
            userFollowingRef = FirebaseDatabase.getInstance().reference.child("Follow")
                .child(firebaseUser.uid).child("Following")
        }
        fun setSomeRefs () {
            val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
            if (pref != null) {
                profileId = pref.getString("profileId", "none").toString()
                postsRef = FirebaseDatabase.getInstance().reference.child("Posts")
                followersRef = FirebaseDatabase.getInstance().reference.child("Follow")
                    .child(profileId).child("Followers")
                followingRef = FirebaseDatabase.getInstance().reference.child("Follow")
                    .child(profileId).child("Following")
            }
        }
        fun setUploadedImages () {
            postList = ArrayList()
            myImagesAdapter = context?.let { MyImagesAdapter(it, postList) }!!
            val uploadedImagesLayoutManager = GridLayoutManager(context, 3)
            profileUploadedImages = fragmentProfileView.findViewById(R.id.profile_uploaded_images)
            profileUploadedImages.setHasFixedSize(true)
            profileUploadedImages.layoutManager = uploadedImagesLayoutManager
            profileUploadedImages.adapter = myImagesAdapter
        }
        fun setSavedImages () {
            mySavedImages = ArrayList()
            savedPostList = ArrayList()
            mySavedImagesAdapter = context?.let { MyImagesAdapter(it, savedPostList) }!!
            val savedImagesLayoutManager = GridLayoutManager(context, 3)
            profileSavedImages = fragmentProfileView.findViewById(R.id.profile_saved_images)
            profileSavedImages.setHasFixedSize(true)
            profileSavedImages.layoutManager = savedImagesLayoutManager
            profileSavedImages.adapter = mySavedImagesAdapter
        }
        setSomeUserProps()
        setSomeRefs()
        setUploadedImages()
        setSavedImages()
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
        fun setPosts () {
            postsRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange (dataSnapshot : DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        var posts = 0
                        for (snapshot in dataSnapshot.children) {
                            if (snapshot.getValue(Post::class.java)?.getPublisher() == profileId) {
                                posts++
                            }
                        }
                        fragmentProfileView.profile_total_posts.text = posts.toString()
                    }
                }

                override fun onCancelled (error : DatabaseError) {
                }
            })
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
        setPosts()
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


    private fun getSavedImages () {
        fun readMySavedImages () {
            postsRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange (dataSnapshot : DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        savedPostList.clear()
                        for (snapshot in dataSnapshot.children) {
                            val post = snapshot.getValue(Post::class.java)
                            for (id in mySavedImages) {
                                if (id == post?.getPostId()) {
                                    savedPostList.add(post)
                                }
                            }
                        }
                        mySavedImagesAdapter.notifyDataSetChanged()
                    }
                }

                override fun onCancelled (error : DatabaseError) {
                }
            })
        }
        FirebaseDatabase.getInstance().reference.child("Saves").child(firebaseUser.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange (dataSnapshot : DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (snapshot in dataSnapshot.children) {
                            (mySavedImages as ArrayList<String>).add(snapshot.key!!)
                        }
                        readMySavedImages()
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
        fun goToShowUsersActivity (title : String) {
            val intent = Intent(context, ShowUsersActivity::class.java)
                .putExtra("id", profileId).putExtra("title", title)
            startActivity(intent)
        }
        fragmentProfileView.profile_total_followers.setOnClickListener {
            goToShowUsersActivity("Followers")
        }
        fragmentProfileView.profile_total_following.setOnClickListener {
            goToShowUsersActivity("Following")
        }
        fragmentProfileView.profile_edit_btn.setOnClickListener {
            handleEditProfileBtnClick()
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
        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", firebaseUser.uid)
        pref?.apply()
    }
}