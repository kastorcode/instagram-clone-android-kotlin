package com.kastorcode.instagramclone.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.Fragments.PostDetailsFragment
import com.kastorcode.instagramclone.Fragments.ProfileFragment
import com.kastorcode.instagramclone.Models.Notification
import com.kastorcode.instagramclone.Models.User
import com.kastorcode.instagramclone.R
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
        fun goToPostDetailsFragment (postId : String) {
            mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                .putString("postId", postId).apply()
            (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PostDetailsFragment()).commit()
        }
        fun goToProfileFragment (profileId : String) {
            mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                .putString("profileId", profileId).apply()
            (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProfileFragment()).commit()
        }
        fun getUserInfo (publisher : String) {
            FirebaseDatabase.getInstance().reference.child("Users")
                .child(publisher).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange (dataSnapshot : DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val user = dataSnapshot.getValue(User::class.java)
                            Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile)
                                .into(holder.notificationsProfileImage)
                            holder.notificationsUsername.text = user.getUserName()
                        }
                    }

                    override fun onCancelled (error : DatabaseError) {
                    }
                })
        }
        fun getPostImage (postId : String) {
            FirebaseDatabase.getInstance().reference.child("Posts").child(postId)
                .child("postImage").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange (dataSnapshot : DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Picasso.get().load(dataSnapshot.value.toString())
                                .placeholder(R.drawable.profile).into(holder.notificationsPostImage)
                        }
                    }

                    override fun onCancelled (error : DatabaseError) {
                    }
                })
        }
        fun setClickListeners (notification : Notification) {
            holder.itemView.setOnClickListener {
                if (notification.getIsPost()) {
                    goToPostDetailsFragment(notification.getPostId())
                }
                else {
                    goToProfileFragment(notification.getUserId())
                }
            }
        }
        val notification = mNotifications[position]
        holder.notificationsComment.text = notification.getText()
        getUserInfo(notification.getUserId())
        if (notification.getIsPost()) {
            holder.notificationsPostImage.visibility = View.VISIBLE
            getPostImage(notification.getPostId())
        }
        else {
            holder.notificationsPostImage.visibility = View.GONE
        }
        setClickListeners(notification)
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