package com.kastorcode.instagramclone.Models


class User {
    private var fullname : String = ""
    private var username : String = ""
    private var email : String = ""
    private var uid : String = ""
    private var bio : String = ""
    private var image : String = ""


    constructor () {}


    constructor (
        fullname : String, username : String, email : String,
        uid : String, bio : String, image : String
    ) {
        setFullName(fullname)
        setUserName(username)
        setEmail(email)
        this.uid = uid
        setBio(bio)
        setImage(image)
    }


    fun getFullName () : String {
        return fullname
    }


    fun setFullName (fullname : String) {
        this.fullname = fullname
    }


    fun getUserName () : String {
        return username
    }


    fun setUserName (username : String) {
        this.username = username
    }


    fun getEmail () : String {
        return email
    }


    fun setEmail (email : String) {
        this.email = email
    }


    fun getUid () : String {
        return uid
    }


    fun getBio () : String {
        return bio
    }


    fun setBio (bio : String) {
        this.bio = bio
    }


    fun getImage () : String {
        return image
    }


    fun setImage (image : String) {
        this.image = image
    }
}