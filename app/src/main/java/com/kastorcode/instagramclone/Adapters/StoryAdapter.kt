package com.kastorcode.instagramclone.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.kastorcode.instagramclone.Models.Story
import com.kastorcode.instagramclone.R
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


    override fun onBindViewHolder (holder : ViewHolder, position : Int) {
        val story = mStory[position]
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