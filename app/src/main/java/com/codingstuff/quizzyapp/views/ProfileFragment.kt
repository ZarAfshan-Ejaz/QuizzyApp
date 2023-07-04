package com.codingstuff.quizzyapp.views

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.Constraints
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.codingstuff.quizzyapp.Model.User
import com.codingstuff.quizzyapp.R
import com.codingstuff.quizzyapp.viewmodel.QuizListViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {
    private var firebaseAuth: FirebaseAuth? = null
    private var navController: NavController? = null
    private val editPass: EditText? = null
    private var edit_f_name: EditText? = null
    private var edit_l_name: EditText? = null
    private var viewModel: QuizListViewModel? = null
    private var deleteAccount: TextView? = null
    private var resetPassword: TextView? = null
    private var editEmail: TextView? = null
    private var updateInfo_btn: Button? = null
    var user_id: String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get<QuizListViewModel>(QuizListViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        user_id = firebaseAuth!!.uid
        navController = Navigation.findNavController(view)
        edit_f_name = view.findViewById(R.id.editFirstNameSignUp_p)
        edit_l_name = view.findViewById(R.id.editLastNameSignUp_p)
        editEmail = view.findViewById(R.id.editEmailSignUp_p)
        resetPassword = view.findViewById(R.id.resetPassword_p)
        deleteAccount = view.findViewById(R.id.tv_deleteAccount_p)
        updateInfo_btn = view.findViewById(R.id.updateInfo_Btn_p)
        fetchUserInfo(user_id, edit_f_name, edit_l_name, editEmail)

        /*
        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_signUpFragment_to_signInFragment);
            }
        });
*/
        deleteAccount!!.setOnClickListener(View.OnClickListener { showDeleteConfirmationDialog(context) })
        resetPassword!!.setOnClickListener(View.OnClickListener { resetPassword(editEmail!!.getText().toString()) })
        updateInfo_btn!!.setOnClickListener(View.OnClickListener { updateUserInfo(user_id, edit_f_name!!.getText().toString(), edit_l_name!!.getText().toString()) })
    }

    fun fetchUserInfo(userId: String?, firstNameTextView: EditText?, lastNameTextView: EditText?, emailTextView: TextView?) {
        val COLLECTION_USERS = "users"
        val db = FirebaseFirestore.getInstance()
        val usersCollectionRef = db.collection(COLLECTION_USERS)
        val userDocRef = usersCollectionRef.document(userId!!)
        userDocRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists()) {
                    val user = document.toObject(User::class.java)

                    // Retrieve the user information
                    val firstName = user!!.first_name
                    val lastName = user.last_name
                    val email = user.email

                    // Set the retrieved information to the TextViews
                    firstNameTextView!!.setText(firstName)
                    lastNameTextView!!.setText(lastName)
                    emailTextView!!.text = email
                } else {
                    Log.d(Constraints.TAG, "No such document")
                }
            } else {
                Log.d(Constraints.TAG, "Failed to fetch user information", task.exception)
            }
        }
    }

    fun resetPassword(email: String?) {
        val auth = FirebaseAuth.getInstance()
        auth.sendPasswordResetEmail(email!!)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Password reset email sent successfully
                        // You can show a success message or perform additional actions here
                        Toast.makeText(context, "Password reset email sent", Toast.LENGTH_SHORT).show()
                    } else {
                        // Failed to send password reset email
                        // You can show an error message or handle the error appropriately
                        Toast.makeText(context, "Failed to send password reset email", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    fun updateUserInfo(userId: String?, newFirstName: String, newLastName: String) {
        val COLLECTION_USERS = "users"
        val db = FirebaseFirestore.getInstance()
        val usersCollectionRef = db.collection(COLLECTION_USERS)
        val userDocRef = usersCollectionRef.document(userId!!)
        val updates: MutableMap<String, Any> = HashMap()
        updates["first_name"] = newFirstName
        updates["last_name"] = newLastName
        userDocRef.update(updates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(Constraints.TAG, "User information updated successfully")
                        Toast.makeText(context, "User information updated successfully", Toast.LENGTH_SHORT).show()
                        // Handle the successful update if needed
                    } else {
                        Toast.makeText(context, "failed to update User information", Toast.LENGTH_SHORT).show()

                        // Handle the update failure appropriately
                    }
                }
    }

    fun showDeleteConfirmationDialog(context: Context?) {
        val builder = AlertDialog.Builder(requireContext()!!)
        builder.setTitle("Confirmation")
                .setMessage("Are you sure you want to delete your account?")
                .setPositiveButton("Delete") { dialog, which -> deleteUserData() }
                .setNegativeButton("Cancel", null)
                .show()
    }

    fun deleteAccount() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            user.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Account deleted successfully", Toast.LENGTH_SHORT).show()
                            navController!!.navigate(R.id.action_profileFragment_to_signUpFragment)
                        } else {
                            Toast.makeText(context, "Failed to delete account", Toast.LENGTH_SHORT).show()
                        }
                    }
        } else {
            // User is already signed out or not available
            // Handle the situation appropriately
        }
    }

    fun deleteUserData() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val userId = user.uid
            val db = FirebaseFirestore.getInstance()
            val usersCollectionRef = db.collection("users")
            usersCollectionRef.document(userId)
                    .delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "User data deleted successfully", Toast.LENGTH_SHORT).show()
                            deleteAccount()
                        } else {
                            Toast.makeText(context, "Failed to delete user data", Toast.LENGTH_SHORT).show()
                        }
                    }
        } else {
            // User is already signed out or not available
            // Handle the situation appropriately
        }
    }
}