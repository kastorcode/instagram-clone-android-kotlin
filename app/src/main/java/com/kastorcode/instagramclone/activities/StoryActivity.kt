package com.kastorcode.instagramclone.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.kastorcode.instagramclone.R
import com.kastorcode.instagramclone.services.navigation.goToShowUsersActivity
import com.kastorcode.instagramclone.services.story.addStoryView
import com.kastorcode.instagramclone.services.story.deleteStory
import com.kastorcode.instagramclone.services.story.getStoryViewsNumber
import com.kastorcode.instagramclone.services.story.getUserStories
import com.kastorcode.instagramclone.services.user.getUser
import com.squareup.picasso.Picasso
import jp.shts.android.storiesprogressview.StoriesProgressView
import kotlinx.android.synthetic.main.activity_story.*
import java.lang.Exception


class StoryActivity : AppCompatActivity() {

    private lateinit var firebaseUserId : String
    private lateinit var userId : String
    private var index = 0
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
        getUserStories(userId, imageList, storyList) {
            storiesProgressView.setStoriesCount(imageList.size)
            storiesProgressView.setStoryDuration(7000L)
            storiesProgressView.setStoriesListener(storiesListener())
            storiesProgressView.startStories(index)
            Picasso.get().load(imageList[index]).placeholder(R.drawable.profile)
                .into(story_image_view)
            addStoryView(userId, storyList[index])
            getStoryViewsNumber(userId, storyList[index]) { count ->
                story_seen_number.text = count
            }
        }
        getUser(userId) { user ->
            Picasso.get().load(user.getImage()).placeholder(R.drawable.profile)
                .into(story_profile_image)
            story_username.text = user.getUserName()
        }
        setClickListeners()
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun setProps () {
        firebaseUserId = FirebaseAuth.getInstance().currentUser!!.uid
        userId = intent.getStringExtra("userId")!!
        index = 0
        pressTime = 0L
        imageList = ArrayList()
        storyList = ArrayList()
        storiesProgressView = findViewById(R.id.story_stories_progress_view)
        onTouchListener = View.OnTouchListener { _, motionEvent ->
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
            story_layout_seen.visibility = View.GONE
            story_delete.visibility = View.GONE
        }
    }


    private fun storiesListener () : StoriesProgressView.StoriesListener {
        return object : StoriesProgressView.StoriesListener {
            override fun onNext () {
                Picasso.get().load(imageList[++index]).placeholder(R.drawable.profile)
                    .into(story_image_view)
                addStoryView(userId, storyList[index])
                getStoryViewsNumber(userId, storyList[index]) { count ->
                    story_seen_number.text = count
                }
            }

            override fun onPrev () {
                if (index == 0) return
                Picasso.get().load(imageList[--index]).placeholder(R.drawable.profile)
                    .into(story_image_view)
                getStoryViewsNumber(userId, storyList[index]) { count ->
                    story_seen_number.text = count
                }
            }

            override fun onComplete () {
                finish()
            }
        }
    }


    private fun setClickListeners () {
        val storySkipView = findViewById<View>(R.id.story_skip)
        storySkipView.setOnTouchListener(onTouchListener)
        storySkipView.setOnClickListener {
            storiesProgressView.skip()
        }
        val storyReverseView = findViewById<View>(R.id.story_reverse)
        storyReverseView.setOnTouchListener(onTouchListener)
        storyReverseView.setOnClickListener {
            storiesProgressView.reverse()
        }
        story_seen_number.setOnClickListener {
            goToShowUsersActivity(this, userId, storyList[index], "Views")
        }
        story_delete.setOnClickListener {
            deleteStory(
                userId,
                storyList[index],
                {
                    Toast.makeText(this, "Story deleted", Toast.LENGTH_LONG).show()
                },
                { exception ->
                    hadExceptionRaised(exception)
                }
            )
        }
    }


    private fun hadExceptionRaised (exception : Exception) {
        Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG).show()
    }
}