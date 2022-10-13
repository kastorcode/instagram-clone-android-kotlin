package com.kastorcode.instagramclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kastorcode.instagramclone.Adapters.UserAdapter
import com.kastorcode.instagramclone.Models.User
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_search.view.*


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
            showUsersToolbar.setNavigationOnClickListener {
                finish()
            }
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
        fun getFollowers () {
            FirebaseDatabase.getInstance().reference.child("Follow").child(id)
                .child("Followers").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange (dataSnapshot : DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            idList.clear()
                            for (snapshot in dataSnapshot.children) {
                                idList.add(snapshot.key!!)
                            }
                            showUsers()
                        }
                    }

                    override fun onCancelled (error : DatabaseError) {
                    }
                })
        }
        fun getFollowing () {
            FirebaseDatabase.getInstance().reference.child("Follow").child(id)
                .child("Following").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange (dataSnapshot : DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            idList.clear()
                            for (snapshot in dataSnapshot.children) {
                                idList.add(snapshot.key!!)
                            }
                            showUsers()
                        }
                    }

                    override fun onCancelled (error : DatabaseError) {
                    }
            })
        }
        fun getLikes () {
            FirebaseDatabase.getInstance().reference.child("Likes").child(id)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange (dataSnapshot : DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            idList.clear()
                            for (snapshot in dataSnapshot.children) {
                                idList.add(snapshot.key!!)
                            }
                            showUsers()
                        }
                    }

                    override fun onCancelled (error : DatabaseError) {
                    }
                })
        }
        fun getViews () {
            FirebaseDatabase.getInstance().reference.child("Stories").child(id)
                .child(intent.getStringExtra("storyId")!!).child("views")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange (dataSnapshot : DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            idList.clear()
                            for (snapshot in dataSnapshot.children) {
                                idList.add(snapshot.key!!)
                            }
                            showUsers()
                        }
                    }

                    override fun onCancelled (error : DatabaseError) {
                    }
                })
        }
        setShowUsersToolbar()
        setShowUsersRecyclerView()
        when (title) {
            "Followers" -> getFollowers()
            "Following" -> getFollowing()
            "Likes" -> getLikes()
            "Views" -> getViews()
        }
    }


    private fun showUsers () {
        FirebaseDatabase.getInstance().reference.child("Users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange (dataSnapshot : DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        userList.clear()
                        for (snapshot in dataSnapshot.children) {
                            val user = snapshot.getValue(User::class.java)
                            for (id in idList) {
                                if (id == user?.getUid()) {
                                    userList.add(user)
                                }
                            }
                        }
                        userAdapter.notifyDataSetChanged()
                    }
                }

                override fun onCancelled (error : DatabaseError) {}
            })
    }
}