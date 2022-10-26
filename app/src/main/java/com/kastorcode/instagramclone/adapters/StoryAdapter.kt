package com.kastorcode.instagramclone.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.kastorcode.instagramclone.models.Story
import com.kastorcode.instagramclone.R
import com.kastorcode.instagramclone.services.navigation.goToStoryActivity
import com.kastorcode.instagramclone.services.story.addOrViewMyStories
import com.kastorcode.instagramclone.services.story.userSawStory
import com.kastorcode.instagramclone.services.user.getUser
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class StoryAdapter (
    private val mContext : Context, private val mStory : List<Story>
) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

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
        val story = mStory[position]
        fun setClickListeners () {
            holder.itemView.setOnClickListener {
                if (holder.adapterPosition == 0) {
                    addOrViewMyStories(mContext, true, story, holder.addStoryTextView)
                }
                else {
                    goToStoryActivity(mContext, story.getUserId())
                }
            }
        }
        getUser(story.getUserId()) { user ->
            Picasso.get().load(user.getImage()).placeholder(R.drawable.profile)
                .into(holder.addStoryIconView)
            if (position != 0) {
                Picasso.get().load(user.getImage()).placeholder(R.drawable.profile)
                    .into(holder.addStoryAddView)
                holder.addStoryTextView.text = user.getUserName()
            }
        }
        if (holder.adapterPosition == 0) {
            addOrViewMyStories(mContext, false, story, holder.addStoryTextView)
        }
        else {
            userSawStory(story.getUserId()) { sawStory ->
                if (sawStory) {
                    holder.addStoryIconView.visibility = View.VISIBLE
                    holder.addStoryAddView.visibility = View.GONE
                }
                else {
                    holder.addStoryIconView.visibility = View.GONE
                    holder.addStoryAddView.visibility = View.VISIBLE
                }
            }
        }
        setClickListeners()
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