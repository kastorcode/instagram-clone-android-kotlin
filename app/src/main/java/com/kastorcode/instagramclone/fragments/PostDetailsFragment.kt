package com.kastorcode.instagramclone.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.kastorcode.instagramclone.adapters.PostAdapter
import com.kastorcode.instagramclone.models.Post
import com.kastorcode.instagramclone.R
import com.kastorcode.instagramclone.services.navigation.goBack
import com.kastorcode.instagramclone.services.post.deletePost
import com.kastorcode.instagramclone.services.post.getPost
import kotlinx.android.synthetic.main.fragment_post_details.*
import java.lang.Exception


class PostDetailsFragment : Fragment() {

    private lateinit var fragmentPostDetails : View
    private lateinit var postId : String
    private lateinit var post : Post
    private lateinit var postList : MutableList<Post>
    private lateinit var postAdapter : PostAdapter


    override fun onCreateView (
        inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?
    ) : View {
        setProps(inflater, container)
        getPost(postId, postList, postAdapter) {
            post = it
            setGuiComponents()
            setClickListeners()
        }
        return fragmentPostDetails
    }


    private fun setProps (inflater : LayoutInflater, container : ViewGroup?) {
        val preferences = context!!.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        postId = preferences.getString("postId", "none")!!
        postList = ArrayList()
        postAdapter = PostAdapter(context!!, postList)
        fragmentPostDetails = inflater.inflate(R.layout.fragment_post_details, container, false)
        val postDetailsRecyclerView = fragmentPostDetails.findViewById<RecyclerView>(R.id.post_details_recycler_view)
        postDetailsRecyclerView.layoutManager = LinearLayoutManager(context)
        postDetailsRecyclerView.adapter = postAdapter
    }


    private fun setGuiComponents () {
        if (post.getPublisher() == FirebaseAuth.getInstance().currentUser!!.uid) {
            post_details_delete_btn.visibility = View.VISIBLE
        }
        else {
            post_details_delete_btn.visibility = View.GONE
        }
    }


    private fun setClickListeners () {
        post_details_delete_btn.setOnClickListener {
            deletePost(post,
                { goBack(activity!!) },
                { exception -> hadExceptionRaised(exception) }
            )
        }
    }


    private fun hadExceptionRaised (exception : Exception) {
        Toast.makeText(context, exception.toString(), Toast.LENGTH_LONG).show()
    }
}