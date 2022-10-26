package com.kastorcode.instagramclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.kastorcode.instagramclone.models.Notification
import com.kastorcode.instagramclone.R
import com.kastorcode.instagramclone.services.navigation.goToPostDetailsFragment
import com.kastorcode.instagramclone.services.navigation.goToProfileFragment
import com.kastorcode.instagramclone.services.post.getPostImage
import com.kastorcode.instagramclone.services.user.getUser
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class NotificationAdapter (
    private val mContext : Context, private val mNotifications : List<Notification>
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder (parent : ViewGroup, viewType : Int) : ViewHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.notifications_item_layout, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder (holder : ViewHolder, position : Int) {
        val notification = mNotifications[position]
        fun setClickListeners () {
            holder.itemView.setOnClickListener {
                if (notification.getIsPost()) {
                    goToPostDetailsFragment(mContext, notification.getPostId())
                }
                else {
                    goToProfileFragment(mContext, notification.getUserId())
                }
            }
        }
        holder.notificationsComment.text = notification.getText()
        getUser(notification.getUserId()) { user ->
            Picasso.get().load(user.getImage()).placeholder(R.drawable.profile)
                .into(holder.notificationsProfileImage)
            holder.notificationsUsername.text = user.getUserName()
        }
        if (notification.getIsPost()) {
            holder.notificationsPostImage.visibility = View.VISIBLE
            getPostImage(notification.getPostId(), holder.notificationsPostImage)
        }
        else {
            holder.notificationsPostImage.visibility = View.GONE
        }
        setClickListeners()
    }


    override fun getItemCount () : Int {
        return mNotifications.size
    }


    inner class ViewHolder (@NonNull itemView : View) : RecyclerView.ViewHolder(itemView) {
        val notificationsProfileImage : CircleImageView = itemView.findViewById(R.id.notifications_profile_image)
        val notificationsUsername : TextView = itemView.findViewById(R.id.notifications_username)
        val notificationsComment : TextView = itemView.findViewById(R.id.notifications_comment)
        val notificationsPostImage : ImageView = itemView.findViewById(R.id.notifications_post_image)
    }
}