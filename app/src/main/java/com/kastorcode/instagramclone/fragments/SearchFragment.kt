package com.kastorcode.instagramclone.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.adapters.UserAdapter
import com.kastorcode.instagramclone.Models.User

import com.kastorcode.instagramclone.R
import kotlinx.android.synthetic.main.fragment_search.view.*


class SearchFragment : Fragment() {

    private var fragmentSearchView : View? = null
    private var recyclerView : RecyclerView? = null
    private var userAdapter : UserAdapter? = null
    private var mUser : MutableList<User>? = null


    override fun onCreateView (
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View? {
        configProps(inflater, container)
        setTextChangedListeners()
        return fragmentSearchView
    }


    private fun configProps (inflater : LayoutInflater, container : ViewGroup?) {
        fragmentSearchView = inflater.inflate(R.layout.fragment_search, container, false)
        recyclerView = fragmentSearchView?.findViewById(R.id.search_recycler_view)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        mUser = ArrayList()
        userAdapter = context?.let {
            UserAdapter(it, mUser as ArrayList<User>, true)
        }
        recyclerView?.adapter = userAdapter
    }


    private fun setTextChangedListeners () {
        fragmentSearchView?.search_edit_text?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged (p0 : CharSequence?, p1 : Int, p2 : Int, p3 : Int) {}
            override fun onTextChanged (input : CharSequence?, start : Int, before : Int, count : Int) {
                if (fragmentSearchView?.search_edit_text?.text.toString() != "") {
                    recyclerView?.visibility = View.VISIBLE
                    retrieveUsers()
                    searchUser(input.toString().lowercase())
                }
            }
            override fun afterTextChanged (p0 : Editable?) {}
        })
    }


    private fun retrieveUsers () {
        FirebaseDatabase.getInstance().getReference().child("Users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange (dataSnapshot : DataSnapshot) {
                    if (fragmentSearchView?.search_edit_text?.text.toString() == "") {
                        mUser?.clear()
                        for (snapshot in dataSnapshot.children) {
                            val user = snapshot.getValue(User::class.java)
                            if (user != null) {
                                mUser?.add(user)
                            }
                        }
                        userAdapter?.notifyDataSetChanged()
                    }
                }
                override fun onCancelled (error : DatabaseError) {}
        })
    }


    private fun searchUser (input : String) {
        FirebaseDatabase.getInstance().getReference().child("Users")
            .orderByChild("fullname").startAt(input).endAt(input + "\uf8ff")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange (dataSnapshot : DataSnapshot) {
                    mUser?.clear()
                    for (snapshot in dataSnapshot.children) {
                        val user = snapshot.getValue(User::class.java)
                        if (user != null) {
                            mUser?.add(user)
                        }
                    }
                    userAdapter?.notifyDataSetChanged()
                }
                override fun onCancelled (error : DatabaseError) {}
        })
    }
}