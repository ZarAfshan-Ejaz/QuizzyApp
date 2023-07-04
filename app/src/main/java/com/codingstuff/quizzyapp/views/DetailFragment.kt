package com.codingstuff.quizzyapp.views

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.codingstuff.quizzyapp.R
import com.codingstuff.quizzyapp.viewmodel.QuizListViewModel

class DetailFragment : Fragment() {
    private var title: TextView? = null
    private var difficulty: TextView? = null
    private var totalQuestions: TextView? = null
    private var startQuizBtn: Button? = null
    private var navController: NavController? = null
    private var position = 0
    private var progressBar: ProgressBar? = null
    private var viewModel: QuizListViewModel? = null
    private var topicImage: ImageView? = null
    private var quizId: String? = null
    private var totalQueCount: Long = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get<QuizListViewModel>(QuizListViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title = view.findViewById(R.id.detailFragmentTitle)
        difficulty = view.findViewById(R.id.detailFragmentDifficulty)
        totalQuestions = view.findViewById(R.id.detailFragmentQuestions)
        startQuizBtn = view.findViewById(R.id.startQuizBtn)
        progressBar = view.findViewById(R.id.detailProgressBar)
        topicImage = view.findViewById(R.id.detailFragmentImage)
        navController = Navigation.findNavController(view)
        position = DetailFragmentArgs.fromBundle(requireArguments()).position
        viewModel!!.quizListLiveData.observe(viewLifecycleOwner) { quizListModels ->
            val quiz = quizListModels[position]
            difficulty!!.setText(quiz.difficulty)
            title!!.setText(quiz.title)
            totalQuestions!!.setText(quiz.questions.toString())
            Glide.with(view).load(quiz.image).into(topicImage!!)
            val handler = Handler()
            handler.postDelayed({ progressBar!!.setVisibility(View.GONE) }, 2000)
            totalQueCount = quiz.questions
            quizId = quiz.quizId
        }
        startQuizBtn!!.setOnClickListener(View.OnClickListener {
            val action = DetailFragmentDirections.actionDetailFragmentToQuizragment()
            action.quizId = quizId!!
            action.totalQueCount = totalQueCount
            navController!!.navigate(action)
        })
    }
}