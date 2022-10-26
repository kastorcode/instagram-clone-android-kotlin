package com.kastorcode.instagramclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.kastorcode.instagramclone.models.Post
import com.kastorcode.instagramclone.R
import com.kastorcode.instagramclone.services.media.copyTextToClipboard
import com.kastorcode.instagramclone.services.navigation.goToPostDetailsFragment
import com.kastorcode.instagramclone.services.navigation.goToProfileFragment
import com.kastorcode.instagramclone.services.navigation.goToPostCommentsActivity
import com.kastorcode.instagramclone.services.navigation.goToShowUsersActivity
import com.kastorcode.instagramclone.services.post.*
import com.kastorcode.instagramclone.services.user.getUser
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception


class PostAdapter (
    private val mContext : Context, private val mPost : List<Post>
) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    override fun onCreateViewHolder (parent : ViewGroup, viewType : Int) : ViewHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.posts_layout, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder (holder : ViewHolder, position : Int) {
        val post = mPost[position]
        fun setClickListeners () {
            holder.postProfileImage.setOnClickListener {
                goToProfileFragment(mContext, post.getPublisher())
            }
            holder.postUsername.setOnClickListener {
                goToProfileFragment(mContext, post.getPublisher())
            }
            holder.postImageView.setOnClickListener {
                goToPostDetailsFragment(mContext, post.getPostId())
            }
            holder.postLikeBtn.setOnClickListener {
                likePost(
                    post.getPostId(),
                    holder.postLikeBtn.tag.toString(),
                    null
                ) { exception ->
                    hadExceptionRaised(exception)
                }
            }
            holder.postCommentBtn.setOnClickListener {
                goToPostCommentsActivity(mContext, post.getPublisher(), post.getPostId())
            }
            holder.postsSaveBtn.setOnClickListener {
                savePost(
                    post.getPostId(),
                    holder.postsSaveBtn.tag.toString(),
                    null
                ) { exception ->
                    hadExceptionRaised(exception)
                }
            }
            holder.postLikes.setOnClickListener {
                goToShowUsersActivity(mContext, post.getPostId(), "", "Likes")
            }
            holder.postPublisher.setOnClickListener {
                goToProfileFragment(mContext, post.getPublisher())
            }
            holder.postDescription.setOnClickListener {
                copyTextToClipboard(mContext, post.getDescription())
            }
            holder.postComments.setOnClickListener {
                goToPostCommentsActivity(mContext, post.getPublisher(), post.getPostId())
            }
        }
        Picasso.get().load(post.getPostImage()).into(holder.postImageView)
        holder.postDescription.text = post.getDescription()
        getUser(post.getPublisher()) { user ->
            Picasso.get().load(user.getImage()).placeholder(R.drawable.profile)
                .into(holder.postProfileImage)
            holder.postUsername.text = user.getUserName()
        }
        isPostLiked(post.getPostId()) { isLiked ->
            if (isLiked) {
                holder.postLikeBtn.setImageResource(R.drawable.heart_clicked)
                holder.postLikeBtn.tag = "Liked"
            }
            else {
                holder.postLikeBtn.setImageResource(R.drawable.heart_not_clicked)
                holder.postLikeBtn.tag = "Like"
            }
        }
        isPostSaved(post.getPostId()) { isSaved ->
            if (isSaved) {
                holder.postsSaveBtn.setImageResource(R.drawable.save_large_icon)
                holder.postsSaveBtn.tag = "Saved"
            }
            else {
                holder.postsSaveBtn.setImageResource(R.drawable.save_unfilled_large_icon)
                holder.postsSaveBtn.tag = "Save"
            }
        }
        getPostLikesCount(post.getPostId()) { count ->
            holder.postLikes.text = "$count likes"
        }
        getPostCommentsCount(post.getPostId()) { count ->
            holder.postComments.text = "$count comments"
        }
        setClickListeners()
    }


    override fun getItemCount () : Int {
        return mPost.size
    }


    private fun hadExceptionRaised (exception : Exception) {
        Toast.makeText(mContext, exception.toString(), Toast.LENGTH_LONG).show()
    }


    inner class ViewHolder (@NonNull itemView : View) : RecyclerView.ViewHolder(itemView) {
        val postProfileImage : CircleImageView = itemView.findViewById(R.id.posts_profile_image)
        val postImageView : ImageView = itemView.findViewById(R.id.posts_image_view)
        val postLikeBtn : ImageView = itemView.findViewById(R.id.posts_like_btn)
        val postCommentBtn : ImageView = itemView.findViewById(R.id.posts_comment_btn)
        val postsSaveBtn : ImageView = itemView.findViewById(R.id.posts_save_btn)
        val postUsername : TextView = itemView.findViewById(R.id.posts_username)
        val postLikes : TextView = itemView.findViewById(R.id.posts_likes)
        val postPublisher : TextView = itemView.findViewById(R.id.posts_publisher)
        val postDescription : TextView = itemView.findViewById(R.id.posts_description)
        val postComments : TextView = itemView.findViewById(R.id.posts_comments)
    }
}