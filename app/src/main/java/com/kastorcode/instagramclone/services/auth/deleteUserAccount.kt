package com.kastorcode.instagramclone.services.auth

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.kastorcode.instagramclone.services.notification.deleteUserNotifications
import com.kastorcode.instagramclone.services.post.deleteUserComments
import com.kastorcode.instagramclone.services.post.deleteUserLikes
import com.kastorcode.instagramclone.services.post.deleteUserPosts
import com.kastorcode.instagramclone.services.post.deleteUserSaves
import com.kastorcode.instagramclone.services.story.deleteUserStories
import com.kastorcode.instagramclone.services.user.*


fun deleteUserAccount (
    email : String, password : String,
    callback : (() -> Unit)? = null, errorCallback : ((exception : Exception) -> Unit)? = null
) {
    fun success () {
        deleteUserComments(
            {
                deleteUserFollow(
                    {
                        deleteUserLikes(
                            {
                                deleteUserNotifications(
                                    {
                                        deleteUserPosts(
                                            {
                                                deleteUserSaves(
                                                    {
                                                        deleteUserStories(
                                                            {
                                                                deleteUserProfileImage(
                                                                    {
                                                                        deleteUserProfileInfo(
                                                                            {
                                                                                deleteFirebaseUser(
                                                                                    callback,
                                                                                    errorCallback
                                                                                )
                                                                            },
                                                                            errorCallback
                                                                        )
                                                                    },
                                                                    errorCallback
                                                                )
                                                            },
                                                            errorCallback
                                                        )
                                                    },
                                                    errorCallback
                                                )
                                            },
                                            errorCallback
                                        )
                                    },
                                    errorCallback
                                )
                            },
                            errorCallback
                        )
                    },
                    errorCallback
                )
            },
            errorCallback
        )
    }

    fun failure (exception : Exception) {
        if (errorCallback != null) {
            errorCallback(exception)
        }
    }

    val credential = EmailAuthProvider.getCredential(email, password)

    FirebaseAuth.getInstance().currentUser!!.reauthenticate(credential)
        .addOnSuccessListener {
            success()
        }
        .addOnFailureListener { exception ->
            failure(exception)
        }
}