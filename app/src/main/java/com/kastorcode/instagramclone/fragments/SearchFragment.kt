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
import com.kastorcode.instagramclone.adapters.UserAdapter
import com.kastorcode.instagramclone.models.User
import com.kastorcode.instagramclone.R
import com.kastorcode.instagramclone.services.user.searchUsers
import kotlinx.android.synthetic.main.fragment_search.view.*


class SearchFragment : Fragment() {

    private lateinit var fragmentSearchView : View
    private lateinit var searchRecyclerView : RecyclerView
    private lateinit var userList : MutableList<User>
    private lateinit var userAdapter : UserAdapter


    override fun onCreateView (
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        setProps(inflater, container)
        setTextChangedListeners()
        return fragmentSearchView
    }


    private fun setProps (inflater : LayoutInflater, container : ViewGroup?) {
        userList = ArrayList()
        fragmentSearchView = inflater.inflate(R.layout.fragment_search, container, false)
        searchRecyclerView = fragmentSearchView.findViewById(R.id.search_recycler_view)
        searchRecyclerView.setHasFixedSize(true)
        searchRecyclerView.layoutManager = LinearLayoutManager(context)
        userAdapter = UserAdapter(context!!, userList, true)
        searchRecyclerView.adapter = userAdapter
    }


    private fun setTextChangedListeners () {
        fragmentSearchView.search_edit_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged (p0 : CharSequence?, p1 : Int, p2 : Int, p3 : Int) {}

            override fun onTextChanged (input : CharSequence?, start : Int, before : Int, count : Int) {
                if (fragmentSearchView.search_edit_text.text.toString() != "") {
                    searchRecyclerView.visibility = View.VISIBLE
                    searchUsers(input.toString().lowercase(), userList, userAdapter)
                }
            }

            override fun afterTextChanged (p0 : Editable?) {}
        })
    }
}