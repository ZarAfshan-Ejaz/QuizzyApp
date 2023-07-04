package com.codingstuff.quizzyapp.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codingstuff.quizzyapp.R

class AppPreferencesFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_app_preferences, container, false)
    }

    companion object {
        fun newInstance(param1: String?, param2: String?): AppPreferencesFragment {
            val fragment = AppPreferencesFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}