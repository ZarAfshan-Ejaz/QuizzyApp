package com.codingstuff.quizzyapp.Model

import com.google.firebase.firestore.DocumentId
data class QuizModel(
        @DocumentId val questionId: String? = null,
        val answer: String? = null,
        val question: String? = null,
        val reason: String? = null,
        val option_a: String? = null,
        val option_b: String? = null,
        val option_c: String? = null,
        val option_d: String? = null
)
