package com.kastorcode.instagramclone.services.user

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth


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