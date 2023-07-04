package com.codingstuff.quizzyapp.Model

class User {
    var first_name: String? = null
    var last_name: String? = null
    var password: String? = null

    // Getters and setters (required for Firestore)
    var email: String? = null

    // Default constructor (required for Firestore)
    constructor() {}
    constructor(first_name: String?, last_name: String?, password: String?, email: String?) {
        this.first_name = first_name
        this.last_name = last_name
        this.password = password
        this.email = email
    }
}