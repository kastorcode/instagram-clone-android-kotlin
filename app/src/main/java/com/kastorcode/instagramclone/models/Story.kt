package com.kastorcode.instagramclone.models


class Story {

    private lateinit var userId : String
    private lateinit var storyId : String
    private lateinit var imageUrl : String
    private var timeStart : Long = 0
    private var timeEnd : Long = 0


    constructor () {}


    constructor (
        userId : String, storyId : String, imageUrl : String, timeStart : Long, timeEnd : Long
    ) {
        this.userId = userId
        this.storyId = storyId
        this.imageUrl = imageUrl
        this.timeStart = timeStart
        this.timeEnd = timeEnd
    }


    fun getUserId () : String {
        return userId
    }


    fun getStoryId () : String {
        return storyId
    }


    fun getImageUrl () : String {
        return imageUrl
    }


    fun getTimeStart () : Long {
        return timeStart
    }


    fun getTimeEnd () : Long {
        return timeEnd
    }
}