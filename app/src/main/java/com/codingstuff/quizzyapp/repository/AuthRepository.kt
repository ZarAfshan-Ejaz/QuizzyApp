package com.codingstuff.quizzyapp.repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.constraintlayout.widget.Constraints
import androidx.lifecycle.MutableLiveData
import com.codingstuff.quizzyapp.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository(private val application: Application) {
    val firebaseUserMutableLiveData: MutableLiveData<FirebaseUser?>
    private val firebaseAuth: FirebaseAuth
    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    init {
        firebaseUserMutableLiveData = MutableLiveData()
        firebaseAuth = FirebaseAuth.getInstance()
    }

    fun signUp(first_name: String?, last_name: String?, password: String?, email: String?) {
        firebaseAuth.createUserWithEmailAndPassword(email!!, password!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                firebaseUserMutableLiveData.postValue(firebaseAuth.currentUser)
                val uidd = firebaseAuth.uid
                storeUserInfo(uidd, first_name, last_name, password, email)
            } else {
                Toast.makeText(application, task.exception!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun signIn(email: String?, pass: String?) {
        firebaseAuth.signInWithEmailAndPassword(email!!, pass!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                firebaseUserMutableLiveData.postValue(firebaseAuth.currentUser)
            } else {
                Toast.makeText(application, task.exception!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    companion object {
        fun storeUserInfo(userId: String?, first_name: String?, last_name: String?, password: String?, email: String?) {
            val COLLECTION_USERS = "users"
            val db = FirebaseFirestore.getInstance()
            val usersCollectionRef = db.collection(COLLECTION_USERS)
            val userDocRef = usersCollectionRef.document(userId!!)
            val user = User(first_name, last_name, password, email)
            userDocRef.set(user)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(Constraints.TAG, "Info stored Successfully", task.exception)

                            // User information stored successfully
                            // You can perform any additional actions here
                        } else {
                            Log.d(Constraints.TAG, "Failed to store user information", task.exception)

                            // Failed to store user information
                            // Handle the error appropriately
                        }
                    }
        }
    }
}