package com.kastorcode.instagramclone.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.AddStoryActivity
import com.kastorcode.instagramclone.Models.Story
import com.kastorcode.instagramclone.Models.User
import com.kastorcode.instagramclone.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class StoryAdapter (
    private val mContext : Context, private val mStory : List<Story>
) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder (parent : ViewGroup, viewType : Int) : ViewHolder {
        if (viewType == 0) {
            val view = LayoutInflater.from(mContext)
                .inflate(R.layout.add_story_item, parent, false)
            return ViewHolder(view)
        }
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.story_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder (holder : ViewHolder, @SuppressLint("RecyclerView") position : Int) {
        fun getUserInfo (userId : String) {
            FirebaseDatabase.getInstance().reference.child("Users")
                .child(userId).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange (dataSnapshot : DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val user = dataSnapshot.getValue(User::class.java)
                            Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile)
                                .into(holder.storyImageView)
                            if (position != 0) {
                                Picasso.get().load(user.getImage()).placeholder(R.drawable.profile)
                                    .into(holder.storyImageSeenView)
                                holder.storyUsernameView.text = user.getUserName()
                            }
                        }
                    }

                    override fun onCancelled (error : DatabaseError) {
                    }
                })
        }
        fun goToAddStoryActivity (story : Story) {
            val intent = Intent(mContext, AddStoryActivity::class.java)
                .putExtra("userId", story.getUserId())
            mContext.startActivity(intent)
        }
        fun setClickListeners (story : Story) {
            holder.itemView.setOnClickListener {
                goToAddStoryActivity(story)
            }
        }
        val story = mStory[position]
        getUserInfo(story.getUserId())
        setClickListeners(story)
    }


    override fun getItemCount () : Int {
        return mStory.size
    }


    override fun getItemViewType (position : Int) : Int {
        if (position == 0) {
            return 0
        }
        return 1
    }


    class ViewHolder (@NonNull itemView : View) : RecyclerView.ViewHolder(itemView) {
        // story_item.xml
        val storyImageView : CircleImageView = itemView.findViewById(R.id.story_image_view)
        val storyImageSeenView : CircleImageView = itemView.findViewById(R.id.story_image_seen_view)
        val storyUsernameView : TextView = itemView.findViewById(R.id.story_username_view)
        // add_story_item.xml
        val addStoryImageView : CircleImageView = itemView.findViewById(R.id.add_story_image_view)
        val addStoryAddView : CircleImageView = itemView.findViewById(R.id.add_story_add_view)
        val addStoryTextView : TextView = itemView.findViewById(R.id.add_story_text_view)
    }
}