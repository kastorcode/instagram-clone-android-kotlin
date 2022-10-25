package com.kastorcode.instagramclone.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.adapters.PostAdapter
import com.kastorcode.instagramclone.Models.Post
import com.kastorcode.instagramclone.R


class PostDetailsFragment : Fragment() {

    private lateinit var fragmentPostDetails : View
    private lateinit var postList : MutableList<Post>
    private lateinit var postAdapter : PostAdapter
    private lateinit var postId : String


    override fun onCreateView (
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        setProps(inflater, container)
        getPost()
        return fragmentPostDetails
    }


    private fun setProps (inflater : LayoutInflater, container : ViewGroup?) {
        val preferences = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        if (preferences != null) {
            postId = preferences.getString("postId", "none")!!
        }
        postList = ArrayList()
        postAdapter = PostAdapter(context!!, postList)
        fragmentPostDetails = inflater.inflate(R.layout.fragment_post_details, container, false)
        val linearLayoutManager = LinearLayoutManager(context)
        val postDetailsRecyclerView = fragmentPostDetails.findViewById<RecyclerView>(R.id.post_details_recycler_view)
        postDetailsRecyclerView.setHasFixedSize(true)
        postDetailsRecyclerView.layoutManager = linearLayoutManager
        postDetailsRecyclerView.adapter = postAdapter
    }


    private fun getPost () {
        FirebaseDatabase.getInstance().reference.child("Posts").child(postId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange (dataSnapshot : DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        postList.clear()
                        val post = dataSnapshot.getValue(Post::class.java)
                        postList.add(post!!)
                        postAdapter.notifyDataSetChanged()
                    }
                }

                override fun onCancelled (error : DatabaseError) {}
            })
    }
}