package com.kastorcode.instagramclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.kastorcode.instagramclone.Adapters.CommentsAdapter
import com.kastorcode.instagramclone.Models.Comment
import com.kastorcode.instagramclone.R
import com.kastorcode.instagramclone.services.media.openImage
import com.kastorcode.instagramclone.services.post.addPostComment
import com.kastorcode.instagramclone.services.post.getPostComments
import com.kastorcode.instagramclone.services.post.getPostImage
import com.kastorcode.instagramclone.services.user.getUser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_post_comments.*
import java.lang.Exception


class PostCommentsActivity : AppCompatActivity() {

    private lateinit var firebaseUserUid : String
    private lateinit var postId : String
    private var postImage : String? = null
    private lateinit var publisher : String
    private lateinit var commentsAdapter : CommentsAdapter
    private lateinit var commentsList : MutableList<Comment>


    override fun onCreate (savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_comments)
        setProps()
        getPostImage(
            postId,
            post_comments_image_comment,
            { postImage = it }
        )
        getUser(firebaseUserUid) { user ->
            Picasso.get().load(user.getImage()).placeholder(R.drawable.profile)
                .into(post_comments_profile_image)
        }
        getPostComments(postId, commentsList, commentsAdapter)
        setClickListeners()
    }


    private fun setProps () {
        firebaseUserUid = FirebaseAuth.getInstance().currentUser!!.uid
        postId = intent.getStringExtra("postId").toString()
        publisher = intent.getStringExtra("publisher").toString()
        commentsList = ArrayList()
        commentsAdapter = CommentsAdapter(this, commentsList)
        val postCommentsRecyclerView : RecyclerView = findViewById(R.id.post_comments_recycler_view)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        postCommentsRecyclerView.layoutManager = linearLayoutManager
        postCommentsRecyclerView.adapter = commentsAdapter
    }


    private fun setClickListeners () {
        post_comments_image_comment.setOnClickListener {
            if (postImage != null) {
                openImage(this, postImage!!)
            }
        }
        post_comments_add_comment.setOnClickListener {
            if (post_comments_write_comment.text.isNotEmpty()) {
                addPostComment(
                    postId,
                    post_comments_write_comment.text.toString(),
                    {
                        post_comments_write_comment.text.clear()
                    },
                    { exception ->
                        hadExceptionRaised(exception)
                    }
                )
            }
        }
    }


    private fun hadExceptionRaised (exception : Exception) {
        Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG).show()
    }
}