package com.kastorcode.instagramclone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.kastorcode.instagramclone.adapters.PostAdapter
import com.kastorcode.instagramclone.adapters.StoryAdapter
import com.kastorcode.instagramclone.models.Post
import com.kastorcode.instagramclone.models.Story

import com.kastorcode.instagramclone.R
import com.kastorcode.instagramclone.services.post.getFollowingPosts
import com.kastorcode.instagramclone.services.story.getFollowingStories
import com.kastorcode.instagramclone.services.user.getUserFollowing


class HomeFragment : Fragment() {

    private lateinit var fragmentHomeView : View
    private lateinit var firebaseUserUid : String
    private lateinit var followingList : MutableList<String>
    private lateinit var postList : MutableList<Post>
    private lateinit var postAdapter : PostAdapter
    private lateinit var storyList : MutableList<Story>
    private lateinit var storyAdapter : StoryAdapter


    override fun onCreateView (
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        setProps(inflater, container)
        setHomeStoryView()
        setHomePostsView()
        getUserFollowing(firebaseUserUid, followingList) {
            getFollowingStories(followingList, storyList, storyAdapter)
            getFollowingPosts(followingList, postList, postAdapter)
        }
        return fragmentHomeView
    }


    private fun setProps (inflater : LayoutInflater, container : ViewGroup?) {
        fragmentHomeView = inflater.inflate(R.layout.fragment_home, container, false)
        firebaseUserUid = FirebaseAuth.getInstance().currentUser!!.uid
        followingList = ArrayList()
        postList = ArrayList()
        postAdapter = PostAdapter(context!!, postList)
        storyList = ArrayList()
        storyList.add(Story(firebaseUserUid, "", "", 0, 0))
        storyAdapter = StoryAdapter(context!!, storyList)
    }


    private fun setHomeStoryView () {
        val homeStoryView = fragmentHomeView.findViewById<RecyclerView>(R.id.home_story_view)
        homeStoryView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        homeStoryView.layoutManager = linearLayoutManager
        homeStoryView.adapter = storyAdapter
    }


    private fun setHomePostsView () {
        val homePostsView = fragmentHomeView.findViewById<RecyclerView>(R.id.home_posts_view)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        homePostsView.layoutManager = linearLayoutManager
        homePostsView.adapter = postAdapter
    }
}