package com.kastorcode.instagramclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kastorcode.instagramclone.models.User
import com.kastorcode.instagramclone.R
import com.kastorcode.instagramclone.services.navigation.goToMainActivity
import com.kastorcode.instagramclone.services.navigation.goToProfileFragment
import com.kastorcode.instagramclone.services.user.followUser
import com.kastorcode.instagramclone.services.user.userIsFollowing
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception


class UserAdapter (
    private val mContext : Context, private val mUser : List<User>,
    private val isFragment : Boolean = false
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private val followingRef : DatabaseReference = FirebaseDatabase.getInstance().reference
        .child("Follow").child(FirebaseAuth.getInstance().currentUser!!.uid)
        .child("Following")


    override fun onCreateViewHolder (parent : ViewGroup, viewType: Int) : ViewHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.user_item_layout, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder (holder : ViewHolder, position : Int) {
        val user = mUser[position]
        fun setClickListeners () {
            holder.itemView.setOnClickListener {
                if (isFragment) {
                    goToProfileFragment(mContext, user.getUid())
                }
                else {
                    goToMainActivity(mContext, user.getUid())
                }
            }
            holder.followButton.setOnClickListener {
                followUser(user.getUid(), holder.followButton.text.toString(), null)
                { exception ->
                    hadExceptionRaised(exception)
                }
            }
        }
        Picasso.get().load(user.getImage()).placeholder(R.drawable.profile)
            .into(holder.imageImageView)
        holder.userNameTextView.text = user.getUserName()
        holder.fullNameTextView.text = user.getFullName()
        userIsFollowing(followingRef, user.getUid()) { isFollowing ->
            if (isFollowing) {
                holder.followButton.text = "Following"
            }
            else {
                holder.followButton.text = "Follow"
            }
        }
        setClickListeners()
    }


    override fun getItemCount () : Int {
        return mUser.size
    }


    private fun hadExceptionRaised (exception : Exception) {
        Toast.makeText(mContext, exception.toString(), Toast.LENGTH_LONG).show()
    }


    class ViewHolder (@NonNull itemView : View) : RecyclerView.ViewHolder(itemView) {
        val imageImageView : CircleImageView = itemView.findViewById(R.id.user_item_image)
        val userNameTextView : TextView = itemView.findViewById(R.id.user_item_username)
        val fullNameTextView : TextView = itemView.findViewById(R.id.user_item_fullname)
        val followButton : Button = itemView.findViewById(R.id.user_item_follow_btn)
    }
}