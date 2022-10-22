package com.kastorcode.instagramclone.Adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.Models.Story
import com.kastorcode.instagramclone.Models.User
import com.kastorcode.instagramclone.R
import com.kastorcode.instagramclone.services.navigation.goToAddStoryActivity
import com.kastorcode.instagramclone.activities.StoryActivity
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class StoryAdapter (
    private val mContext : Context, private val mStory : List<Story>
) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    private val firebaseUserUid = FirebaseAuth.getInstance().currentUser!!.uid


    override fun onCreateViewHolder (parent : ViewGroup, viewType : Int) : ViewHolder {
        if (viewType == 0) {
            val view = LayoutInflater.from(mContext)
                .inflate(R.layout.add_story_item, parent, false)
            return ViewHolder(view, viewType)
        }
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.story_item, parent, false)
        return ViewHolder(view, viewType)
    }


    override fun onBindViewHolder (holder : ViewHolder, @SuppressLint("RecyclerView") position : Int) {
        fun seenStory (userId : String) {
            FirebaseDatabase.getInstance().reference.child("Stories").child(userId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange (dataSnapshot : DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            var i = 0
                            for (snapshot in dataSnapshot.children) {
                                if (!snapshot.child("views").child(firebaseUserUid).exists() &&
                                    System.currentTimeMillis() < snapshot.getValue(Story::class.java)!!.getTimeEnd()
                                ) {
                                    i++
                                }
                            }
                            if (i > 0) {
                                holder.addStoryIconView.visibility = View.VISIBLE
                                holder.addStoryAddView.visibility = View.GONE
                            }
                            else {
                                holder.addStoryIconView.visibility = View.GONE
                                holder.addStoryAddView.visibility = View.VISIBLE
                            }
                        }
                    }

                    override fun onCancelled (error : DatabaseError) {}
                })
        }
        fun goToStoryActivity () {
            val intent = Intent(mContext, StoryActivity::class.java)
                .putExtra("userId", firebaseUserUid)
            mContext.startActivity(intent)
        }
        fun myStories (click : Boolean, story : Story) {
            FirebaseDatabase.getInstance().reference.child("Stories").child(firebaseUserUid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange (dataSnapshot : DataSnapshot) {
                        var hasStory = false
                        val timeCurrent = System.currentTimeMillis()
                        for (snapshot in dataSnapshot.children) {
                            val story = snapshot.getValue(Story::class.java)
                            if (timeCurrent > story!!.getTimeStart() &&
                                timeCurrent < story.getTimeEnd()
                            ) {
                                hasStory = true
                                break
                            }
                        }
                        if (click) {
                            if (hasStory) {
                                val alertDialog = AlertDialog.Builder(mContext).create()
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "View Story")
                                { dialogInterface, which ->
                                    goToStoryActivity()
                                    dialogInterface.dismiss()
                                }
                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add Story")
                                { dialogInterface, which ->
                                    goToAddStoryActivity(mContext, story.getUserId())
                                    dialogInterface.dismiss()
                                }
                                alertDialog.show()
                            }
                            else {
                                goToAddStoryActivity(mContext, story.getUserId())
                            }
                        }
                        else {
                            if (hasStory) {
                                holder.addStoryTextView.text = "My Story"
                            }
                            else {
                                holder.addStoryTextView.text = "Add Story"
                            }
                        }
                    }

                    override fun onCancelled (error : DatabaseError) {}
                })
        }
        fun getUserInfo (userId : String) {
            FirebaseDatabase.getInstance().reference.child("Users")
                .child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange (dataSnapshot : DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val user = dataSnapshot.getValue(User::class.java)
                            Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile)
                                .into(holder.addStoryIconView)
                            if (position != 0) {
                                Picasso.get().load(user.getImage()).placeholder(R.drawable.profile)
                                    .into(holder.addStoryAddView)
                                holder.addStoryTextView.text = user.getUserName()
                            }
                        }
                    }

                    override fun onCancelled (error : DatabaseError) {
                    }
                })
        }
        fun setClickListeners (story : Story) {
            holder.itemView.setOnClickListener {
                if (holder.adapterPosition == 0) {
                    myStories(true, story)
                }
                else {
                    goToStoryActivity()
                }
            }
        }
        val story = mStory[position]
        getUserInfo(story.getUserId())
        if (holder.adapterPosition == 0) {
            myStories(false, story)
        }
        else {
            seenStory(story.getUserId())
        }
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


    class ViewHolder (@NonNull itemView : View, viewType : Int) : RecyclerView.ViewHolder(itemView) {
        var addStoryIconView : CircleImageView
        var addStoryAddView : CircleImageView
        var addStoryTextView : TextView
        init {
            if (viewType == 0) {
                // add_story_item.xml
                addStoryIconView = itemView.findViewById(R.id.add_story_icon_view)
                addStoryAddView = itemView.findViewById(R.id.add_story_add_view)
                addStoryTextView = itemView.findViewById(R.id.add_story_text_view)
            }
            else {
                // story_item.xml
                addStoryIconView = itemView.findViewById(R.id.story_icon_view)
                addStoryAddView = itemView.findViewById(R.id.story_icon_seen_view)
                addStoryTextView = itemView.findViewById(R.id.story_username_view)
            }
        }
    }
}