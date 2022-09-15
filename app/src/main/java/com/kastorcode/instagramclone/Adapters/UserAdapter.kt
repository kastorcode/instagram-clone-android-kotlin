package com.kastorcode.instagramclone.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.kastorcode.instagramclone.Models.User
import com.kastorcode.instagramclone.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class UserAdapter (
    private val mContext : Context, private val mUser : List<User>,
    private val isFragment : Boolean = false
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun onCreateViewHolder (parent : ViewGroup, viewType: Int) : UserAdapter.ViewHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.user_item_layout, parent, false)
        return UserAdapter.ViewHolder(view)
    }


    override fun onBindViewHolder (holder : UserAdapter.ViewHolder, position : Int) {
        val user = mUser[position]
        Picasso.get().load(user.getImage())
            .placeholder(R.drawable.profile).into(holder.imageImageView)
        holder.userNameTextView.text = user.getUserName()
        holder.fullNameTextView.text = user.getFullName()
        holder.followButton
    }


    override fun getItemCount () : Int {
        return mUser.size
    }


    class ViewHolder (@NonNull itemView : View) : RecyclerView.ViewHolder(itemView) {
        val imageImageView : CircleImageView = itemView.findViewById(R.id.user_item_image)
        val userNameTextView : TextView = itemView.findViewById(R.id.user_item_username)
        val fullNameTextView : TextView = itemView.findViewById(R.id.user_item_fullname)
        val followButton : Button = itemView.findViewById(R.id.user_item_follow_btn)
    }
}