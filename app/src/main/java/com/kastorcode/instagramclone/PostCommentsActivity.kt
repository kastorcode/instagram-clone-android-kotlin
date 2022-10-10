package com.kastorcode.instagramclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.Adapters.CommentsAdapter
import com.kastorcode.instagramclone.Models.Comment
import com.kastorcode.instagramclone.Models.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_post_comments.*


class PostCommentsActivity : AppCompatActivity() {

    private lateinit var firebaseUser : FirebaseUser
    private lateinit var postId : String
    private lateinit var publisher : String
    private lateinit var commentsAdapter : CommentsAdapter
    private lateinit var commentsList : MutableList<Comment>


    override fun onCreate (savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_comments)
        setProps()
        getPostImage()
        getUserInfo()
        getComments()
        setClickListeners()
    }


    private fun setProps () {
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
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


    private fun getPostImage () {
        FirebaseDatabase.getInstance().reference.child("Posts")
            .child(postId).child("postImage")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange (dataSnapshot : DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Picasso.get().load(dataSnapshot.value.toString())
                            .placeholder(R.drawable.profile).into(post_comments_image_comment)
                    }
                }

                override fun onCancelled (error : DatabaseError) {
                }
            })
    }


    private fun getUserInfo () {
        FirebaseDatabase.getInstance().reference.child("Users")
            .child(firebaseUser.uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange (dataSnapshot : DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val user = dataSnapshot.getValue(User::class.java)
                        Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile)
                            .into(post_comments_profile_image)
                    }
                }

                override fun onCancelled (error : DatabaseError) {
                }
            })
    }


    private fun getComments () {
        FirebaseDatabase.getInstance().reference.child("Comments").child(postId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange (dataSnapshot : DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        commentsList.clear()
                        for (snapshot in dataSnapshot.children) {
                            val comment = snapshot.getValue(Comment::class.java)
                            commentsList.add(comment!!)
                        }
                        commentsAdapter.notifyDataSetChanged()
                    }
                }

                override fun onCancelled (error : DatabaseError) {
                }
            })
    }


    private fun setClickListeners () {
        fun addComment () {
            if (post_comments_write_comment.text.isEmpty()) {
                return
            }
            val commentMap = HashMap<String, String>()
            commentMap["comment"] = post_comments_write_comment.text.toString()
            commentMap["publisher"] = firebaseUser.uid
            FirebaseDatabase.getInstance().reference.child("Comments").child(postId).push()
                .setValue(commentMap).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        post_comments_write_comment.text.clear()
                        addNotification(commentMap["comment"]!!)
                    }
                    else {
                        Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()
                    }
                }
        }
        post_comments_add_comment.setOnClickListener {
            addComment()
        }
    }


    private fun addNotification (comment : String) {
        val notificationMap = HashMap<String, Any>()
        notificationMap["userId"] = firebaseUser.uid
        notificationMap["postId"] = postId
        notificationMap["isPost"] = true
        notificationMap["text"] = "commented: $comment"
        FirebaseDatabase.getInstance().reference.child("Notifications").child(publisher)
            .push().setValue(notificationMap)
    }
}