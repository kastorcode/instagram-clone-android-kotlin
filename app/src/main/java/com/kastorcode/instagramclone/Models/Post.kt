package com.kastorcode.instagramclone.Models


class Post {
    private lateinit var postId : String
    private lateinit var postImage : String
    private lateinit var publisher : String
    private lateinit var description : String


    constructor () {}


    constructor (postId : String, postImage : String, publisher : String, description : String) {
        this.postId = postId
        this.postImage = postImage
        this.publisher = publisher
        this.description = description
    }


    fun getPostId () : String {
        return postId
    }


    fun getPostImage () : String {
        return postImage
    }


    fun setPostImage (postImage : String) {
        this.postImage = postImage
    }


    fun getPublisher () : String {
        return publisher
    }


    fun getDescription () : String {
        return description
    }


    fun setDescription (description : String) {
        this.description = description
    }
}