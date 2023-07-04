package com.codingstuff.quizzyapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codingstuff.quizzyapp.Model.QuizListModel
import com.codingstuff.quizzyapp.repository.QuizListRepository
import com.codingstuff.quizzyapp.repository.QuizListRepository.onFirestoreTaskComplete

class QuizListViewModel : ViewModel(), onFirestoreTaskComplete {
    val quizListLiveData = MutableLiveData<List<QuizListModel>>()
    private val repository = QuizListRepository(this)

    init {
        repository.getQuizData()
    }

    override fun quizDataLoaded(quizListModels: List<QuizListModel>) {
        quizListLiveData.value = quizListModels
    }

    override fun onError(e: Exception) {
        Log.d("QuizERROR", "onError: " + e.message)
    }
}