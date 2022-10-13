package com.kastorcode.instagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.kastorcode.instagramclone.Models.Story
import com.kastorcode.instagramclone.Models.User
import com.squareup.picasso.Picasso
import jp.shts.android.storiesprogressview.StoriesProgressView
import kotlinx.android.synthetic.main.activity_story.*
import kotlin.properties.Delegates


class StoryActivity : AppCompatActivity() {

    private lateinit var firebaseUserId : String
    private lateinit var userId : String
    private var counter = 0
    private var pressTime = 0L
    private val pressLimit = 500L
    private lateinit var imageList : MutableList<String>
    private lateinit var storyList : MutableList<String>
    private lateinit var storiesProgressView : StoriesProgressView
    private lateinit var onTouchListener: View.OnTouchListener


    override fun onCreate (savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story)
        setProps()
        setGuiComponents()
        getUserStories()
        getUserInfo()
        setClickListeners()
    }


    private fun setProps () {
        firebaseUserId = FirebaseAuth.getInstance().currentUser!!.uid
        userId = intent.getStringExtra("userId")!!
        counter = 0
        pressTime = 0L
        imageList = ArrayList()
        storyList = ArrayList()
        storiesProgressView = findViewById(R.id.story_stories_progress_view)
        onTouchListener = View.OnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    pressTime = System.currentTimeMillis()
                    storiesProgressView.pause()
                    return@OnTouchListener false
                }
                MotionEvent.ACTION_UP -> {
                    val now = System.currentTimeMillis()
                    storiesProgressView.resume()
                    return@OnTouchListener pressLimit < now - pressTime
                }
            }
            false
        }
    }


    private fun setGuiComponents () {
        if (firebaseUserId == userId) {
            story_layout_seen.visibility = View.VISIBLE
            story_delete.visibility = View.VISIBLE
        }
        else {
            story_layout_seen.visibility = View.INVISIBLE
            story_delete.visibility = View.INVISIBLE
        }
    }


    private fun getUserStories () {
        FirebaseDatabase.getInstance().reference.child("Stories").child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener,
                StoriesProgressView.StoriesListener {
                override fun onDataChange (dataSnapshot : DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        imageList.clear()
                        storyList.clear()
                        val timeCurrent = System.currentTimeMillis()
                        for (snapshot in dataSnapshot.children) {
                            val story = snapshot.getValue(Story::class.java)
                            if (timeCurrent > story!!.getTimeStart() && timeCurrent < story.getTimeEnd()) {
                                imageList.add(story.getImageUrl())
                                storyList.add(story.getStoryId())
                            }
                        }
                        storiesProgressView.setStoriesCount(imageList.size)
                        storiesProgressView.setStoryDuration(5000L)
                        storiesProgressView.setStoriesListener(this)
                        storiesProgressView.startStories(counter)
                        Picasso.get().load(imageList[counter]).placeholder(R.drawable.profile)
                            .into(story_image_view)
                        addViewToStory(storyList[counter])
                        getStorySeenNumber(storyList[counter])
                    }
                }

                override fun onCancelled (error : DatabaseError) {}

                override fun onNext () {
                    Picasso.get().load(imageList[++counter]).placeholder(R.drawable.profile)
                        .into(story_image_view)
                    addViewToStory(storyList[counter])
                    getStorySeenNumber(storyList[counter])
                }

                override fun onPrev () {
                    Picasso.get().load(imageList[--counter]).placeholder(R.drawable.profile)
                        .into(story_image_view)
                    getStorySeenNumber(storyList[counter])
                }

                override fun onComplete () {
                    finish()
                }
            })
    }


    private fun getUserInfo () {
        FirebaseDatabase.getInstance().reference.child("Users")
            .child(userId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange (dataSnapshot : DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val user = dataSnapshot.getValue(User::class.java)
                        Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile)
                            .into(story_profile_image)
                        story_username.text = user.getUserName()
                    }
                }

                override fun onCancelled (error : DatabaseError) {
                }
            })
    }


    private fun setClickListeners () {
        fun goToShowUsersActivity () {
            val intent = Intent(this, ShowUsersActivity::class.java)
                .putExtra("id", userId).putExtra("storyId", storyList[counter])
                .putExtra("title", "Views")
            startActivity(intent)
        }
        fun deleteStory () {
            FirebaseDatabase.getInstance().reference.child("Stories").child(userId)
                .child(storyList[counter]).removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Story deleted", Toast.LENGTH_LONG).show()
                    }
                }
        }
        val storySkipView = findViewById<View>(R.id.story_skip)
        storySkipView.setOnTouchListener(onTouchListener)
        storySkipView.setOnClickListener { storiesProgressView.skip() }
        val storyReverseView = findViewById<View>(R.id.story_reverse)
        storyReverseView.setOnTouchListener(onTouchListener)
        storyReverseView.setOnClickListener { storiesProgressView.reverse() }
        story_seen_number.setOnClickListener { goToShowUsersActivity() }
        story_delete.setOnClickListener { deleteStory() }
    }


    private fun addViewToStory (storyId : String) {
        FirebaseDatabase.getInstance().reference.child("Stories").child(userId)
            .child(storyId).child("views").child(firebaseUserId).setValue(true)
    }


    private fun getStorySeenNumber (storyId : String) {
        FirebaseDatabase.getInstance().reference.child("Stories").child(userId)
            .child(storyId).child("views").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange (dataSnapshot : DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        story_seen_number.text = dataSnapshot.childrenCount.toString()
                    }
                }

                override fun onCancelled (error : DatabaseError) {}
            })
    }
}