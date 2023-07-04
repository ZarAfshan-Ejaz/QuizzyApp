package com.codingstuff.quizzyapp.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.codingstuff.quizzyapp.R

class HelpFragment : Fragment() {
    private var navController: NavController? = null
    private val ll_app_preferences: LinearLayout? = null
    private val ll_study_reminder: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        /*  ll_app_preferences = view.findViewById(R.id.ll_app_preferences);
        ll_study_reminder = view.findViewById(R.id.ll_study_reminder);

        ll_app_preferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_settingFragment_to_appPreferencesFragment);
            }
        });
        ll_study_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_settingFragment_to_studyReminderFragment);
            }
        });*/

        return inflater.inflate(R.layout.fragment_help, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    fun navigateTo_otherScreens() {}

    companion object {
        fun newInstance(param1: String?, param2: String?): HelpFragment {
            val fragment = HelpFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}