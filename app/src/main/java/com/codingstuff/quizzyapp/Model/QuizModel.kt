package com.codingstuff.quizzyapp.Model

import com.google.firebase.firestore.DocumentId
data class QuizModel(
        var questionId: String? = null,
        val answer: String? = null,
        val question: String? = null,
        val reason: String? = null,
        val option_a: String? = null,
        val option_b: String? = null,
        val option_c: String? = null,
        val option_d: String? = null,
        val date: String? = null,
        val domain: String? = null,
        var domain_name: String? = null,
        val subject: String? = null,
        val select_opt : String? = null,
        var flagged : Boolean? = false,
        var attempted : Boolean? = false,
        var correct : Boolean? = false,



        )
