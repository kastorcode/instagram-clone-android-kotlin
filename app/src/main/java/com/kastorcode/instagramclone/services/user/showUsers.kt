package com.kastorcode.instagramclone.services.user

import android.annotation.SuppressLint
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.Adapters.UserAdapter
import com.kastorcode.instagramclone.Models.User


@SuppressLint("NotifyDataSetChanged")
fun showUsers (
    idList : MutableList<String>, userList : MutableList<User>, userAdapter : UserAdapter,
    callback : (() -> Unit)? = null
) {
    val usersRef = FirebaseDatabase.getInstance().reference.child("Users")
    userList.clear()

    fun finish () {
        userAdapter.notifyDataSetChanged()
        if (callback != null) {
            callback()
        }
    }

    fun getUser (index : Int) {
        if (index == idList.size) {
            finish()
        }
        else {
            usersRef.child(idList[index]).addValueEventListener(object : ValueEventListener {
                override fun onDataChange (dataSnapshot : DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        userList.add(dataSnapshot.getValue(User::class.java)!!)
                    }
                    getUser(index + 1)
                }

                override fun onCancelled (error : DatabaseError) {}
            })
        }
    }

    getUser(0)
}