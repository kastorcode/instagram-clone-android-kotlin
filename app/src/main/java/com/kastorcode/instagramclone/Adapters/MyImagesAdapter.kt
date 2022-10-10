package com.kastorcode.instagramclone.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.kastorcode.instagramclone.Fragments.PostDetailsFragment
import com.kastorcode.instagramclone.Models.Post
import com.kastorcode.instagramclone.R
import com.squareup.picasso.Picasso


class MyImagesAdapter (
    private val mContext : Context, private val mPosts : List<Post>
) : RecyclerView.Adapter<MyImagesAdapter.ViewHolder>() {

    override fun onCreateViewHolder (parent : ViewGroup, viewType : Int) : ViewHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.my_images_item_layout, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder (holder : ViewHolder, position : Int) {
        fun goToPostDetailsFragment (post : Post) {
            mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                .putString("postId", post.getPostId()).apply()
            (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PostDetailsFragment()).commit()
        }
        fun setClickListeners (post : Post) {
            holder.myImagesImage.setOnClickListener {
                goToPostDetailsFragment(post)
            }
        }
        val post = mPosts[position]
        Picasso.get().load(post.getPostImage()).into(holder.myImagesImage)
        setClickListeners(post)
    }


    override fun getItemCount () : Int {
        return mPosts.size
    }


    inner class ViewHolder (@NonNull itemView : View) : RecyclerView.ViewHolder(itemView) {
        val myImagesImage : ImageView = itemView.findViewById(R.id.my_images_image)
    }
}