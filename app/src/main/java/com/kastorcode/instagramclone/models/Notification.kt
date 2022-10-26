package com.kastorcode.instagramclone.models


class Notification {

    private lateinit var userId : String
    private lateinit var postId : String
    private var isPost : Boolean = false
    private lateinit var text : String


    constructor () {}


    constructor (userId : String, postId : String, isPost : Boolean, text : String) {
        this.userId = userId
        this.postId = postId
        this.isPost = isPost
        this.text = text
    }


    fun getUserId () : String {
        return userId
    }


    fun getPostId () : String {
        return postId
    }


    fun getIsPost () : Boolean {
        return isPost
    }


    fun getText () : String {
        return text
    }
}