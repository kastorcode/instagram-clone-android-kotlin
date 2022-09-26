package com.kastorcode.instagramclone.Adapters

import android.content.Context
import android.content.Intent
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
import com.kastorcode.instagramclone.PostCommentsActivity
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
        fun getPublisherInfo (publisher : String) {
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
        fun isPostLike (postId : String) {
            FirebaseDatabase.getInstance().reference.child("Likes").child(postId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange (dataSnapshot : DataSnapshot) {
                        if (dataSnapshot.child(firebaseUser!!.uid).exists()) {
                            holder.postLikeBtn.setImageResource(R.drawable.heart_clicked)
                            holder.postLikeBtn.tag = "Liked"
                        }
                        else {
                            holder.postLikeBtn.setImageResource(R.drawable.heart_not_clicked)
                            holder.postLikeBtn.tag = "Like"
                        }
                    }

                    override fun onCancelled (error : DatabaseError) {
                    }
                })
        }
        fun getPostLikes (postId : String) {
            FirebaseDatabase.getInstance().reference.child("Likes").child(postId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange (dataSnapshot : DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            holder.postLikes.text = "${dataSnapshot.childrenCount} likes"
                        }
                        else {
                            holder.postLikes.text = "0 likes"
                        }
                    }

                    override fun onCancelled (error : DatabaseError) {
                    }
                })
        }
        fun getPostComments (postId : String) {
            FirebaseDatabase.getInstance().reference.child("Comments").child(postId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange (dataSnapshot : DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            holder.postComments.text = "${dataSnapshot.childrenCount} comments"
                        }
                        else {
                            holder.postComments.text = "0 comments"
                        }
                    }

                    override fun onCancelled (error : DatabaseError) {
                    }
                })
        }
        fun setClickListeners (postId : String, publisher : String) {
            fun likePost () {
                FirebaseDatabase.getInstance().reference.child("Likes").child(postId)
                    .child(firebaseUser!!.uid).setValue(true)
            }
            fun unLikePost () {
                FirebaseDatabase.getInstance().reference.child("Likes").child(postId)
                    .child(firebaseUser!!.uid).removeValue()
            }
            holder.postLikeBtn.setOnClickListener {
                if (holder.postLikeBtn.tag == "Like") {
                    likePost()
                }
                else {
                    unLikePost()
                }
            }
            holder.postCommentBtn.setOnClickListener {
                val intent = Intent(mContext, PostCommentsActivity::class.java)
                intent.putExtra("postId", postId)
                intent.putExtra("publisher", publisher)
                mContext.startActivity(intent)
            }
        }
        val post = mPost[position]
        Picasso.get().load(post.getPostImage()).into(holder.postImageView)
        holder.postDescription.text = post.getDescription()
        getPublisherInfo(post.getPublisher())
        isPostLike(post.getPostId())
        getPostLikes(post.getPostId())
        getPostComments(post.getPostId())
        setClickListeners(post.getPostId(), post.getPublisher())
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