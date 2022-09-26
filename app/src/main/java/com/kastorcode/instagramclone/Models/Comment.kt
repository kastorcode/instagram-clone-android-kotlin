package com.kastorcode.instagramclone.Models


class Comment {

    private lateinit var comment : String
    private lateinit var publisher : String


    constructor () {}


    constructor (comment : String, publisher : String) {
        setComment(comment)
        this.publisher = publisher
    }


    fun getComment () : String {
        return comment
    }


    fun setComment (comment : String) {
        this.comment = comment
    }


    fun getPublisher () : String {
        return publisher
    }
}