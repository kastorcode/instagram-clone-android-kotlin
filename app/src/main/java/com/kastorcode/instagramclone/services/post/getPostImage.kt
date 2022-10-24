package com.kastorcode.instagramclone.services.post

import android.widget.ImageView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.R
import com.squareup.picasso.Picasso


fun getPostImage (
    postId : String, imageView : ImageView? = null,
    callback : ((postImage : String) -> Unit)? = null, errorCallback : ((exception : Exception) -> Unit)? = null
) {
    fun success (postImage : String) {
        if (imageView != null) {
            Picasso.get().load(postImage).placeholder(R.drawable.profile).into(imageView)
        }
        if (callback != null) {
            callback(postImage)
        }
    }

    fun failure (exception : Exception) {
        if (errorCallback != null) {
            errorCallback(exception)
        }
    }

    FirebaseDatabase.getInstance().reference.child("Posts").child(postId)
        .child("postImage").addValueEventListener(object : ValueEventListener {
            override fun onDataChange (dataSnapshot : DataSnapshot) {
                if (dataSnapshot.exists()) {
                    success(dataSnapshot.value.toString())
                }
                else {
                    failure(Exception("404"))
                }
            }

            override fun onCancelled (error : DatabaseError) {}
        })
}