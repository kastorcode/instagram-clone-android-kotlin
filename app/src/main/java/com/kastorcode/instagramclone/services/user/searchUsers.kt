package com.kastorcode.instagramclone.services.user

import android.annotation.SuppressLint
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.models.User
import com.kastorcode.instagramclone.adapters.UserAdapter


fun searchUsers (
    input : String, userList : MutableList<User>, userAdapter : UserAdapter,
    callback : (() -> Unit)? = null
) {
    FirebaseDatabase.getInstance().reference.child("Users").orderByChild("fullname")
        .startAt(input).endAt(input + "\uf8ff").addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange (dataSnapshot : DataSnapshot) {
                userList.clear()
                for (snapshot in dataSnapshot.children) {
                    userList.add(snapshot.getValue(User::class.java)!!)
                }
                userAdapter.notifyDataSetChanged()
                if (callback != null) {
                    callback()
                }
            }

            override fun onCancelled (error : DatabaseError) {}
        })
}