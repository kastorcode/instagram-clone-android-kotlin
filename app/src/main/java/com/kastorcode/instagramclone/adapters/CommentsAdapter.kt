package com.kastorcode.instagramclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.kastorcode.instagramclone.Models.Comment
import com.kastorcode.instagramclone.R
import com.kastorcode.instagramclone.services.media.copyTextToClipboard
import com.kastorcode.instagramclone.services.navigation.goToProfileFragment
import com.kastorcode.instagramclone.services.user.getUser
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class CommentsAdapter (
    private val mContext : Context, private val mComments : List<Comment>
) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    override fun onCreateViewHolder (parent : ViewGroup, viewType : Int) : ViewHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.comments_item_layout, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder (holder : ViewHolder, position : Int) {
        val comment = mComments[position]
        fun setClickListeners () {
            holder.commentsProfileImage.setOnClickListener {
                goToProfileFragment(mContext, comment.getPublisher())
            }
            holder.commentsUsername.setOnClickListener {
                goToProfileFragment(mContext, comment.getPublisher())
            }
            holder.commentsComment.setOnClickListener {
                copyTextToClipboard(mContext, comment.getComment())
            }
        }
        holder.commentsComment.text = comment.getComment()
        getUser(comment.getPublisher()) { user ->
            Picasso.get().load(user.getImage()).placeholder(R.drawable.profile)
                .into(holder.commentsProfileImage)
            holder.commentsUsername.text = user.getUserName()
        }
        setClickListeners()
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