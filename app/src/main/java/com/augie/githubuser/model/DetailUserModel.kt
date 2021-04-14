package com.augie.githubuser.model

data class DetailUserModel (
    var name: String = "",
    var email: String = "",
    var location: String = "",
    var company: String = "",
    var follower: String = "0",
    var following: String = "0",
    var repository: String = "0",
    var photo: String = ""
)