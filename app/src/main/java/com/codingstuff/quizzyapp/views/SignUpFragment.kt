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

class SignUpFragment : Fragment() {
    private var viewModel: AuthViewModel? = null
    private var navController: NavController? = null
    private var editEmail: EditText? = null
    private var editPass: EditText? = null
    private var edit_f_name: EditText? = null
    private var edit_l_name: EditText? = null
    private var signInText: TextView? = null
    private var signUpBtn: Button? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        edit_f_name = view.findViewById(R.id.editFirstNameSignUp)
        edit_l_name = view.findViewById(R.id.editLastNameSignUp)
        editEmail = view.findViewById(R.id.editEmailSignUp)
        editPass = view.findViewById(R.id.editPassSignUp)
        signInText = view.findViewById(R.id.signInText)
        signUpBtn = view.findViewById(R.id.signUpBtn)
        signInText!!.setOnClickListener(View.OnClickListener { navController!!.navigate(R.id.action_signUpFragment_to_signInFragment) })
        signUpBtn!!.setOnClickListener(View.OnClickListener {
            val email = editEmail!!.getText().toString()
            val pass = editPass!!.getText().toString()
            val f_name = edit_f_name!!.getText().toString()
            val l_name = edit_l_name!!.getText().toString()
            if (!email.isEmpty() && !pass.isEmpty() && !f_name.isEmpty() && !l_name.isEmpty()) {
                viewModel!!.signUp(f_name, l_name, pass, email)
                Toast.makeText(context, "Registered Successfully", Toast.LENGTH_SHORT).show()
                viewModel!!.firebaseUserMutableLiveData.observe(viewLifecycleOwner, object : Observer<FirebaseUser?> {
                    override fun onChanged(firebaseUser: FirebaseUser?) {
                        if (firebaseUser != null) {
                            navController!!.navigate(R.id.action_signUpFragment_to_signInFragment)
                        }
                    }
                })
            } else {
                Toast.makeText(context, "Please Enter Complete Information", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get<AuthViewModel>(AuthViewModel::class.java)
    }
}