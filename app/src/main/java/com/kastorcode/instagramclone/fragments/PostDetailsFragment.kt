package com.kastorcode.instagramclone.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kastorcode.instagramclone.adapters.PostAdapter
import com.kastorcode.instagramclone.models.Post
import com.kastorcode.instagramclone.R
import com.kastorcode.instagramclone.services.post.getPost


class PostDetailsFragment : Fragment() {

    private lateinit var fragmentPostDetails : View
    private lateinit var postId : String
    private lateinit var postList : MutableList<Post>
    private lateinit var postAdapter : PostAdapter


    override fun onCreateView (
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        setProps(inflater, container)
        getPost(postId, postList, postAdapter)
        return fragmentPostDetails
    }


    private fun setProps (inflater : LayoutInflater, container : ViewGroup?) {
        val preferences = context!!.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        postId = preferences.getString("postId", "none")!!
        postList = ArrayList()
        postAdapter = PostAdapter(context!!, postList)
        fragmentPostDetails = inflater.inflate(R.layout.fragment_post_details, container, false)
        val linearLayoutManager = LinearLayoutManager(context)
        val postDetailsRecyclerView = fragmentPostDetails.findViewById<RecyclerView>(R.id.post_details_recycler_view)
        postDetailsRecyclerView.setHasFixedSize(true)
        postDetailsRecyclerView.layoutManager = linearLayoutManager
        postDetailsRecyclerView.adapter = postAdapter
    }
}