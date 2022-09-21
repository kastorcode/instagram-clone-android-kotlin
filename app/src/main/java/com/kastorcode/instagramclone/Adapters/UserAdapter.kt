package com.kastorcode.instagramclone.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.kastorcode.instagramclone.Fragments.ProfileFragment
import com.kastorcode.instagramclone.Models.User
import com.kastorcode.instagramclone.R
import com.kastorcode.instagramclone.Services.followUser
import com.kastorcode.instagramclone.Services.unfollowUser
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class UserAdapter (
    private val mContext : Context, private val mUser : List<User>,
    private val isFragment : Boolean = false
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private var firebaseUser : FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private val followingRef : DatabaseReference = firebaseUser?.uid.let {
        FirebaseDatabase.getInstance().reference.child("Follow")
            .child(it.toString()).child("Following")
    }


    override fun onCreateViewHolder (parent : ViewGroup, viewType: Int) : UserAdapter.ViewHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.user_item_layout, parent, false)
        return UserAdapter.ViewHolder(view)
    }


    override fun onBindViewHolder (holder : UserAdapter.ViewHolder, position : Int) {
        fun checkFollowingStatus (uid : String, followButton : Button) {
            followingRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange (snapshot : DataSnapshot) {
                    if (snapshot.child(uid).exists()) {
                        followButton.text = "Following"
                    }
                    else {
                        followButton.text = "Follow"
                    }
                }

                override fun onCancelled (error : DatabaseError) {
                }
            })
        }
        fun setClickListeners (user : User) {
            holder.itemView.setOnClickListener(View.OnClickListener {
                val pref = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                pref.putString("profileId", user.getUid())
                pref.apply()
                (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ProfileFragment()).commit()
            })
            holder.followButton.setOnClickListener {
                if (holder.followButton.text.toString() == "Follow") {
                    followUser(user.getUid())
                }
                else {
                    unfollowUser(user.getUid())
                }
            }
        }

        val user = mUser[position]
        Picasso.get().load(user.getImage())
            .placeholder(R.drawable.profile).into(holder.imageImageView)
        holder.userNameTextView.text = user.getUserName()
        holder.fullNameTextView.text = user.getFullName()
        checkFollowingStatus(user.getUid(), holder.followButton)
        setClickListeners(user)
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