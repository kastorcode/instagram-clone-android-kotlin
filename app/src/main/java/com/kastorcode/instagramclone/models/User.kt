package com.kastorcode.instagramclone.models


class User {

    private lateinit var fullname : String
    private lateinit var username : String
    private lateinit var email : String
    private lateinit var uid : String
    private lateinit var bio : String
    private lateinit var image : String


    constructor () {}


    constructor (
        fullname : String, username : String, email : String,
        uid : String, bio : String, image : String
    ) {
        this.fullname = fullname
        this.username = username
        this.email = email
        this.uid = uid
        this.bio = bio
        this.image = image
    }


    fun getFullName () : String {
        return fullname
    }


    fun getUserName () : String {
        return username
    }


    fun getEmail () : String {
        return email
    }


    fun getUid () : String {
        return uid
    }


    fun getBio () : String {
        return bio
    }


    fun getImage () : String {
        return image
    }
}