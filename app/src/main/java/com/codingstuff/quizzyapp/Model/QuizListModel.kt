package com.codingstuff.quizzyapp.Model

import com.google.firebase.firestore.DocumentId

class QuizListModel {
    @DocumentId
    var quizId: String? = null
    var title: String? = null
    var image: String? = null
    var difficulty: String? = null
    var questions: Long = 0

    constructor() {}
    constructor(quizId: String?, title: String?, image: String?, difficulty: String?, questions: Long) {
        this.quizId = quizId
        this.title = title
        this.image = image
        this.difficulty = difficulty
        this.questions = questions
    }
}