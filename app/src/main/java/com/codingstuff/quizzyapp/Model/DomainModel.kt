package com.codingstuff.quizzyapp.Model

import com.google.firebase.firestore.DocumentId
data class DomainModel(
        val domainId: String? = null,
        var domain_name: String? = null,
        val correct: String? = null,
        val incorrect : String? = null,
        var totalQuiz : String? = null,
        var progress_percentage : String? = null
        )
