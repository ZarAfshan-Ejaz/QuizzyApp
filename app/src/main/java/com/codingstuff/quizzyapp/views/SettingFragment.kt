package com.codingstuff.quizzyapp.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.codingstuff.quizzyapp.R

class SettingFragment : Fragment() {
    private var navController: NavController? = null
    private var ll_app_preferences: LinearLayout? = null
    private var ll_study_reminder: LinearLayout? = null
    private var ll_help: LinearLayout? = null
    private var ll_about_ATP: LinearLayout? = null
    private var tv_exam_settings: TextView? = null
    private var tv_switch_exam: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        ll_app_preferences = view.findViewById(R.id.ll_app_preferences)
        ll_study_reminder = view.findViewById(R.id.ll_study_reminder)
        ll_help = view.findViewById(R.id.ll_help)
        ll_about_ATP = view.findViewById(R.id.ll_about_ATP)
        tv_exam_settings = view.findViewById(R.id.tv_exam_settings)
        tv_switch_exam = view.findViewById(R.id.tv_switch_exam)
        tv_switch_exam!!.setOnClickListener(View.OnClickListener { navController!!.navigate(R.id.action_settingFragment_to_switchExamDialogFragment) })
        tv_exam_settings!!.setOnClickListener(View.OnClickListener { navController!!.navigate(R.id.action_settingFragment_to_examSettingFragment) })
        ll_app_preferences!!.setOnClickListener(View.OnClickListener { navController!!.navigate(R.id.action_settingFragment_to_appPreferencesFragment) })
        ll_study_reminder!!.setOnClickListener(View.OnClickListener { navController!!.navigate(R.id.action_settingFragment_to_studyReminderFragment) })
        ll_help!!.setOnClickListener(View.OnClickListener { navController!!.navigate(R.id.action_settingFragment_to_helpFragment) })
        ll_about_ATP!!.setOnClickListener(View.OnClickListener { navController!!.navigate(R.id.action_settingFragment_to_aboutFragment) })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    fun navigateTo_otherScreens() {}

    companion object {
        fun newInstance(param1: String?, param2: String?): SettingFragment {
            val fragment = SettingFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}