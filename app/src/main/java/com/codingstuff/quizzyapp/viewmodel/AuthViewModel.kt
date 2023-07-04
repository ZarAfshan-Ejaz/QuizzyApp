package com.codingstuff.quizzyapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.codingstuff.quizzyapp.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    val firebaseUserMutableLiveData: MutableLiveData<FirebaseUser?>
    val currentUser: FirebaseUser?
    private val repository: AuthRepository

    init {
        repository = AuthRepository(application)
        currentUser = repository.currentUser
        firebaseUserMutableLiveData = repository.firebaseUserMutableLiveData
    }

    fun signUp(first_name: String?, last_name: String?, password: String?, email: String?) {
        repository.signUp(first_name, last_name, password, email)
    }

    fun signIn(email: String?, pass: String?) {
        repository.signIn(email, pass)
    }

    fun signOut() {
        repository.signOut()
    }
}