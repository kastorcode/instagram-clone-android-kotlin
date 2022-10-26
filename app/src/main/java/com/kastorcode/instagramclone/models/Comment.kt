package com.kastorcode.instagramclone.models


class Comment {

    private lateinit var comment : String
    private lateinit var publisher : String


    constructor () {}


    constructor (comment : String, publisher : String) {
        this.comment = comment
        this.publisher = publisher
    }


    fun getComment () : String {
        return comment
    }


    fun getPublisher () : String {
        return publisher
    }
}