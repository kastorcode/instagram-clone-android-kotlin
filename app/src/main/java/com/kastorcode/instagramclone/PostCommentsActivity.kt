package com.kastorcode.instagramclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.Models.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_post_comments.*


class PostCommentsActivity : AppCompatActivity() {

    private lateinit var firebaseUser : FirebaseUser
    private lateinit var postId : String
    private lateinit var publisher : String


    override fun onCreate (savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_comments)
        setProps()
        getUserInfo()
        setClickListeners()
    }


    private fun setProps () {
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        postId = intent.getStringExtra("postId").toString()
        publisher = intent.getStringExtra("publisher").toString()
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


    private fun setClickListeners () {
        fun addComment () {
            if (post_comments_write_comment.text.isEmpty()) {
                return
            }
            val commentMap = HashMap<String, Any>()
            commentMap["comment"] = post_comments_write_comment.text.toString()
            commentMap["publisher"] = firebaseUser.uid
            FirebaseDatabase.getInstance().reference.child("Comments").child(postId).push()
                .setValue(commentMap).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        post_comments_write_comment.text.clear()
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
}