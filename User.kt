package com.example.deliveryapp

internal class User {
    var displayname: String


    var email: String
    var createdAt: Long = 0

    constructor() {}
    constructor(displayname: String, email: String, createdAt: Long) {
        this.displayname = displayname
        this.email = email
        this.createdAt = createdAt
    }

}
