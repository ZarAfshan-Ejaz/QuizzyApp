package com.codingstuff.quizzyapp.Model

import com.google.firebase.firestore.DocumentId

class QuestionModel {
    @DocumentId
    var questionId: String? = null
    var answer: String? = null
    var question: String? = null
    var option_a: String? = null
    var option_b: String? = null
    var option_c: String? = null
    var timer: Long = 0

    constructor() {}
    constructor(questionId: String?, answer: String?, question: String?, option_a: String?, option_b: String?, option_c: String?, timer: Long) {
        this.questionId = questionId
        this.answer = answer
        this.question = question
        this.option_a = option_a
        this.option_b = option_b
        this.option_c = option_c
        this.timer = timer
    }
}