package com.kastorcode.instagramclone.services.story

import android.app.AlertDialog
import android.content.Context
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.kastorcode.instagramclone.Models.Story
import com.kastorcode.instagramclone.services.navigation.goToAddStoryActivity
import com.kastorcode.instagramclone.services.navigation.goToStoryActivity


fun addOrViewMyStories (
    context : Context, click : Boolean, story : Story, textView : TextView
) {
    val firebaseUserUid = FirebaseAuth.getInstance().currentUser!!.uid

    fun showAddOrView () {
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "View Story")
        { dialogInterface, _ ->
            goToStoryActivity(context, firebaseUserUid)
            dialogInterface.dismiss()
        }
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add Story")
        { dialogInterface, _ ->
            goToAddStoryActivity(context, story.getUserId())
            dialogInterface.dismiss()
        }
        alertDialog.show()
    }

    userHasStory(firebaseUserUid) { hasStory ->
        if (click) {
            if (hasStory) {
                showAddOrView()
            }
            else {
                goToAddStoryActivity(context, story.getUserId())
            }
        }
        else {
            if (hasStory) {
                textView.text = "My Story"
            }
            else {
                textView.text = "Add Story"
            }
        }
    }
}