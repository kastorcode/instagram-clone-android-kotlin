package com.kastorcode.instagramclone.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.kastorcode.instagramclone.Adapters.PostAdapter
import com.kastorcode.instagramclone.Models.Post

import com.kastorcode.instagramclone.R


class HomeFragment : Fragment() {

    private lateinit var fragmentHomeView : View
    private lateinit var firebaseUser : FirebaseUser
    private lateinit var followingRef : DatabaseReference
    private lateinit var followingList : MutableList<String>
    private lateinit var postsList : MutableList<Post>
    private lateinit var postAdapter : PostAdapter


    override fun onCreateView (
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        setProps(inflater, container)
        setHomeRecyclerView()
        getFollowing()
        return fragmentHomeView
    }


    private fun setProps (inflater : LayoutInflater, container : ViewGroup?) {
        fragmentHomeView = inflater.inflate(R.layout.fragment_home, container, false)
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        followingRef = FirebaseDatabase.getInstance().reference.child("Follow")
            .child(firebaseUser.uid).child("Following")
        followingList = ArrayList()
        postsList = ArrayList()
        postAdapter = PostAdapter(context!!, postsList as ArrayList<Post>)
    }


    private fun setHomeRecyclerView () {
        val homeRecyclerView = fragmentHomeView.findViewById<RecyclerView>(R.id.home_recycler_view)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        homeRecyclerView.layoutManager = linearLayoutManager
        homeRecyclerView.adapter = postAdapter
    }


    private fun getFollowing () {
        fun getPosts () {
            FirebaseDatabase.getInstance().reference.child("Posts")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange (dataSnapshot : DataSnapshot) {
                        postsList.clear()
                        for (snapshot in dataSnapshot.children) {
                            val post = snapshot.getValue(Post::class.java)
                            for (following in followingList) {
                                if (post!!.getPublisher() == following) {
                                    postsList.add(post)
                                }
                            }
                        }
                        postAdapter.notifyDataSetChanged()
                    }

                    override fun onCancelled (error : DatabaseError) {}
                })
        }
        followingRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange (snapshot : DataSnapshot) {
                if (snapshot.exists()) {
                    followingList.clear()
                    for (following in snapshot.children) {
                        following.key?.let { followingList.add(it) }
                    }
                    getPosts()
                }
            }

            override fun onCancelled (error : DatabaseError) {}
        })
    }
}