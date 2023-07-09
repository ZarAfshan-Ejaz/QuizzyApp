package com.codingstuff.quizzyapp.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.codingstuff.quizzyapp.R
import com.codingstuff.quizzyapp.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseUser

class SignInFragment : Fragment() {
    private var viewModel: AuthViewModel? = null
    private var navController: NavController? = null
    private var editEmail: EditText? = null
    private var editPass: EditText? = null
    private var signUpText: TextView? = null
    private var signInBtn: Button? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        editEmail = view.findViewById(R.id.emailEditSignIN)
        editPass = view.findViewById(R.id.passEditSignIn)
        signUpText = view.findViewById(R.id.signUpText)
        signInBtn = view.findViewById(R.id.signInBtn)
        signUpText!!.setOnClickListener(View.OnClickListener { navController!!.navigate(R.id.action_signInFragment_to_signUpFragment) })
        signInBtn!!.setOnClickListener(View.OnClickListener {
            val email = editEmail!!.getText().toString()
            val pass = editPass!!.getText().toString()
            if (!email.isEmpty() && !pass.isEmpty()) {
                viewModel!!.signIn(email, pass)
                Toast.makeText(context, "Login Successfully", Toast.LENGTH_SHORT).show()
                viewModel!!.firebaseUserMutableLiveData.observe(viewLifecycleOwner, object : Observer<FirebaseUser?> {
                    override fun onChanged(firebaseUser: FirebaseUser?) {
                        if (firebaseUser != null) {
                            //navController!!.navigate(R.id.action_signInFragment_to_listFragment)
                            navController!!.navigate(R.id.action_signInFragment_to_studyFragment)
                        }
                    }

                })
            } else {
                Toast.makeText(context, "Please Enter Email and Pass", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get<AuthViewModel>(AuthViewModel::class.java)
    }
}