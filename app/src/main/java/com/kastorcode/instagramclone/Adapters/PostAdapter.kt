package com.kastorcode.instagramclone.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.Models.Post
import com.kastorcode.instagramclone.Models.User
import com.kastorcode.instagramclone.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class PostAdapter (
    private val mContext : Context, private val mPost : List<Post>,
    private val isFragment : Boolean = false
) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private val firebaseUser : FirebaseUser? = FirebaseAuth.getInstance().currentUser


    override fun onCreateViewHolder (parent : ViewGroup, viewType : Int) : ViewHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.posts_layout, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder (holder : ViewHolder, position : Int) {
        fun getPublisherInfo (publisher : String, holder : ViewHolder) {
            FirebaseDatabase.getInstance().reference.child("Users").child(publisher)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange (snapshot : DataSnapshot) {
                        if (snapshot.exists()) {
                            val user = snapshot.getValue(User::class.java)
                            Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile)
                                .into(holder.postProfileImage)
                            holder.postUsername.text = user.getUserName()
                        }
                    }

                    override fun onCancelled (error : DatabaseError) {}
                })
        }
        val post = mPost[position]
        Picasso.get().load(post.getPostImage()).into(holder.postImageView)
        getPublisherInfo(post.getPublisher(), holder)
    }


    override fun getItemCount () : Int {
        return mPost.size
    }


    inner class ViewHolder (@NonNull itemView : View) : RecyclerView.ViewHolder(itemView) {
        val postProfileImage : CircleImageView = itemView.findViewById(R.id.posts_profile_image)
        val postImageView : ImageView = itemView.findViewById(R.id.posts_image_view)
        val postLikeBtn : ImageView = itemView.findViewById(R.id.posts_like_btn)
        val postCommentBtn : ImageView = itemView.findViewById(R.id.posts_comment_btn)
        val postSaveCommentBtn : ImageView = itemView.findViewById(R.id.posts_save_comment_btn)
        val postUsername : TextView = itemView.findViewById(R.id.posts_username)
        val postLikes : TextView = itemView.findViewById(R.id.posts_likes)
        val postPublisher : TextView = itemView.findViewById(R.id.posts_publisher)
        val postDescription : TextView = itemView.findViewById(R.id.posts_description)
        val postComments : TextView = itemView.findViewById(R.id.posts_comments)
    }
}