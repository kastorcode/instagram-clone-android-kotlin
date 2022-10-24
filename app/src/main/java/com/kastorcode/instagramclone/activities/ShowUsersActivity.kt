package com.kastorcode.instagramclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kastorcode.instagramclone.Adapters.UserAdapter
import com.kastorcode.instagramclone.Models.User
import com.kastorcode.instagramclone.R
import com.kastorcode.instagramclone.services.post.getPostLikes
import com.kastorcode.instagramclone.services.story.getStoryViews
import com.kastorcode.instagramclone.services.user.getUserFollowers
import com.kastorcode.instagramclone.services.user.getUserFollowing
import com.kastorcode.instagramclone.services.user.showUsers


class ShowUsersActivity : AppCompatActivity() {

    private lateinit var id : String
    private lateinit var title : String
    private lateinit var userAdapter : UserAdapter
    private lateinit var userList : MutableList<User>
    private lateinit var idList : MutableList<String>


    override fun onCreate (savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_users)
        setProps()
        setGuiComponents()
    }


    override fun onSupportNavigateUp () : Boolean {
        finish()
        return true
    }


    private fun setProps () {
        id = intent.getStringExtra("id")!!
        title = intent.getStringExtra("title")!!
        idList = ArrayList()
        userList = ArrayList()
        userAdapter = UserAdapter(this, userList, false)
    }


    private fun setGuiComponents () {
        fun setShowUsersToolbar () {
            val showUsersToolbar = findViewById<Toolbar>(R.id.show_users_toolbar)
            setSupportActionBar(showUsersToolbar)
            supportActionBar?.title = title
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        fun setShowUsersRecyclerView () {
            val showUsersRecyclerView = findViewById<RecyclerView>(R.id.show_users_recycler_view)
            showUsersRecyclerView.setHasFixedSize(true)
            showUsersRecyclerView.layoutManager = LinearLayoutManager(this)
            showUsersRecyclerView.adapter = userAdapter
        }
        setShowUsersToolbar()
        setShowUsersRecyclerView()
        when (title) {
            "Followers" -> getUserFollowers(id, idList) { showUsers(idList, userList, userAdapter) }
            "Following" -> getUserFollowing(id, idList) { showUsers(idList, userList, userAdapter) }
            "Likes" -> getPostLikes(id, idList) { showUsers(idList, userList, userAdapter) }
            "Views" -> getStoryViews(id, intent.getStringExtra("storyId")!!, idList) { showUsers(idList, userList, userAdapter) }
        }
    }
}