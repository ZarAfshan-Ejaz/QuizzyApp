package com.codingstuff.quizzyapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codingstuff.quizzyapp.Model.QuestionModel
import com.codingstuff.quizzyapp.repository.QuestionRepository
import com.codingstuff.quizzyapp.repository.QuestionRepository.OnQuestionLoad
import com.codingstuff.quizzyapp.repository.QuestionRepository.OnResultAdded
import com.codingstuff.quizzyapp.repository.QuestionRepository.OnResultLoad

class QuestionViewModel : ViewModel(), OnQuestionLoad, OnResultAdded, OnResultLoad {
    val questionMutableLiveData: MutableLiveData<List<QuestionModel?>?>
    private val repository: QuestionRepository
    val resultMutableLiveData: MutableLiveData<HashMap<String, Long?>?>
    val results: Unit
        get() {
            repository.results
        }

    init {
        questionMutableLiveData = MutableLiveData()
        resultMutableLiveData = MutableLiveData()
        repository = QuestionRepository(this, this, this)
    }

    fun addResults(resultMap: HashMap<String?, Any?>?) {
        repository.addResults(resultMap)
    }

    fun setQuizId(quizId: String?) {
        repository.setQuizId(quizId)
    }

    val questions: Unit
        get() {
            repository.questions
        }

    override fun onLoad(questionModels: List<QuestionModel?>?) {
        questionMutableLiveData.value = questionModels
    }

    override fun onSubmit(): Boolean {
        return true
    }

    override fun onResultLoad(resultMap: HashMap<String, Long?>?) {
        resultMutableLiveData.value = resultMap
    }

    override fun onError(e: Exception?) {
        Log.d("QuizError", "onError: " + e!!.message)
    }
}