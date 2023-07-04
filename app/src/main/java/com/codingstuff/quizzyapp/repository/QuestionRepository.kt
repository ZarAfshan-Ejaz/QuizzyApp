package com.codingstuff.quizzyapp.repository

import com.codingstuff.quizzyapp.Model.QuestionModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class QuestionRepository(onQuestionLoad: OnQuestionLoad, onResultAdded: OnResultAdded, onResultLoad: OnResultLoad) {
    private val firebaseFirestore: FirebaseFirestore
    private var quizId: String? = null
    private val resultMap = HashMap<String, Long?>()
    private val onQuestionLoad: OnQuestionLoad
    private val onResultAdded: OnResultAdded
    private val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    private val onResultLoad: OnResultLoad
    val results: Unit
        get() {
            firebaseFirestore.collection("Quiz").document(quizId!!)
                    .collection("results").document(currentUserId)
                    .get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            resultMap["correct"] = task.result.getLong("correct")
                            resultMap["wrong"] = task.result.getLong("wrong")
                            resultMap["notAnswered"] = task.result.getLong("notAnswered")
                            onResultLoad.onResultLoad(resultMap)
                        } else {
                            onResultLoad.onError(task.exception)
                        }
                    }
        }

    fun addResults(resultMap: HashMap<String?, Any?>?) {
        firebaseFirestore.collection("Quiz").document(quizId!!)
                .collection("results").document(currentUserId)
                .set(resultMap!!).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onResultAdded.onSubmit()
                    } else {
                        onResultAdded.onError(task.exception)
                    }
                }
    }

    fun setQuizId(quizId: String?) {
        this.quizId = quizId
    }

    init {
        firebaseFirestore = FirebaseFirestore.getInstance()
        this.onQuestionLoad = onQuestionLoad
        this.onResultAdded = onResultAdded
        this.onResultLoad = onResultLoad
    }

    val questions: Unit
        get() {
            firebaseFirestore.collection("Quiz").document(quizId!!)
                    .collection("questions").get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            onQuestionLoad.onLoad(task.result.toObjects(QuestionModel::class.java))
                        } else {
                            onQuestionLoad.onError(task.exception)
                        }
                    }
        }

    interface OnResultLoad {
        fun onResultLoad(resultMap: HashMap<String, Long?>?)
        fun onError(e: Exception?)
    }

    interface OnQuestionLoad {
        fun onLoad(questionModels: List<QuestionModel?>?)
        fun onError(e: Exception?)
    }

    interface OnResultAdded {
        fun onSubmit(): Boolean
        fun onError(e: Exception?)
    }
}