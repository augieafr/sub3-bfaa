package com.augie.githubuser.model

data class DetailUserModel (
    var name: String = "",
    var email: String = "",
    var location: String = "",
    var company: String = "",
    var follower: Int = 0,
    var following: Int = 0,
    var repository: Int = 0,
    var photo: String = ""
)