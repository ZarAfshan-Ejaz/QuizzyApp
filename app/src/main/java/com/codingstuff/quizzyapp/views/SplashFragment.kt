package com.codingstuff.quizzyapp.views

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.codingstuff.quizzyapp.R
import com.codingstuff.quizzyapp.viewmodel.AuthViewModel

class SplashFragment : Fragment() {
    private var viewModel: AuthViewModel? = null
    private var navController: NavController? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get<AuthViewModel>(AuthViewModel::class.java)
        navController = Navigation.findNavController(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val handler = Handler()
        handler.postDelayed({
            if (viewModel!!.currentUser != null) {

                // navController.navigate(R.id.action_splashFragment_to_listFragment);
                navController!!.navigate(R.id.action_splashFragment_to_studyFragment2)
            } else {
                navController!!.navigate(R.id.action_splashFragment_to_signInFragment)
            }
        }, 4000)
    }
}