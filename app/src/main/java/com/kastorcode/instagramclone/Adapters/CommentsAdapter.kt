package com.kastorcode.instagramclone.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.Models.Comment
import com.kastorcode.instagramclone.Models.User
import com.kastorcode.instagramclone.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_post_comments.*


class CommentsAdapter (
    private val mContext : Context, private val mComments : List<Comment>
) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    private val firebaseUser : FirebaseUser? = FirebaseAuth.getInstance().currentUser


    override fun onCreateViewHolder (parent : ViewGroup, viewType : Int) : ViewHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.comments_item_layout, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder (holder : ViewHolder, position : Int) {
        fun getUserInfo (publisher : String) {
            FirebaseDatabase.getInstance().reference.child("Users")
                .child(publisher).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange (dataSnapshot : DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val user = dataSnapshot.getValue(User::class.java)
                            Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile)
                                .into(holder.commentsProfileImage)
                            holder.commentsUsername.text = user.getUserName()
                        }
                    }

                    override fun onCancelled (error : DatabaseError) {
                    }
                })
        }
        val comment = mComments[position]
        holder.commentsComment.text = comment.getComment()
        getUserInfo(comment.getPublisher())
    }


    override fun getItemCount () : Int {
        return mComments.size
    }


    inner class ViewHolder (@NonNull itemView : View) : RecyclerView.ViewHolder(itemView) {
        val commentsProfileImage : CircleImageView = itemView.findViewById(R.id.comments_profile_image)
        val commentsUsername : TextView = itemView.findViewById(R.id.comments_username)
        val commentsComment : TextView = itemView.findViewById(R.id.comments_comment)
    }
}