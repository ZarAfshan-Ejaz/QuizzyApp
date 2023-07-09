package com.codingstuff.quizzyapp.views

import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codingstuff.quizzyapp.Adapter.QuizAdapter
import com.codingstuff.quizzyapp.Adapter.RecyclerViewAdapter
import com.codingstuff.quizzyapp.Model.QuizModel
import com.codingstuff.quizzyapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class QuizFragment : Fragment() {
    private lateinit var countdownTimer: CountDownTimer
    val quizList = mutableListOf<QuizModel>()
    private var recyclerView: RecyclerView? = null
    private var leftArrow: ImageButton? = null
    private var rightArrow: ImageButton? = null
    lateinit var tv_timer: TextView
    lateinit var btn_submit: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_quiz2, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        leftArrow = view.findViewById(R.id.leftArrow)
        rightArrow = view.findViewById(R.id.rightArrow)
        tv_timer = view.findViewById<TextView>(R.id.tv_timer)
        btn_submit = view.findViewById<Button>(R.id.btn_submit)


        val args = arguments
        val startTimer = args?.getBoolean("START_TIMER")
        val millisecondsSelected = args?.getLong("millisecondsSelected")
        val no_Of_Quiz = args?.getInt("NO-Of_Quiz")
        val quiz_CAT = args?.getString("QUIZ_CAT")

        if (quiz_CAT == "timed_quiz") {
            if (millisecondsSelected != null) {
                startTimer(millisecondsSelected)
                getQuiz()

            }
        }
        if (quiz_CAT == "missed_quiz") {
            if (no_Of_Quiz != null) {
                getMissedQuiz(no_Of_Quiz)

            }
        }
        if (quiz_CAT == "QUICK_10_QUIZ") {
            getRandomQuestions()
        }
        if (quiz_CAT == "QotD") {
            rightArrow?.visibility = View.GONE
            leftArrow?.visibility = View.GONE
            tv_timer?.visibility = View.GONE

            question_of_the_day()
        }
        leftArrow!!.setOnClickListener(View.OnClickListener { scrollRecyclerView(-1) })
        rightArrow!!.setOnClickListener(View.OnClickListener { scrollRecyclerView(1) })
        // Create a LinearLayoutManager and set it as the layout manager for the RecyclerView
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView!!.setLayoutManager(layoutManager)

        // Create an array of items for the RecyclerView
        val items = arrayOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")

        // Create the adapter and set it to the RecyclerView
        //val adapter = RecyclerViewAdapter(items)
        //recyclerView!!.setAdapter(adapter)
        //getQuiz()
        // getRandomQuestions()
        return view
    }

    private fun question_of_the_day() {
        val firestore = FirebaseFirestore.getInstance()
        val collectionPath = "/Exams/CEReGwH8PMG8zXVQqP3B/ComputerScience/Ay8RWesFncvEBVmuIAx0/Questions"
        val collectionRef = firestore.collection(collectionPath)

        collectionRef.get().addOnSuccessListener { querySnapshot ->
            val allQuestions = querySnapshot.toObjects(QuizModel::class.java)

            val shuffledQuestions = allQuestions.shuffled()
            val randomQuestions = shuffledQuestions.take(1)

            val quizAdapter = QuizAdapter(randomQuestions)
            recyclerView!!.adapter = quizAdapter
            collectAndStoreDocuments(randomQuestions)
        }.addOnFailureListener { exception ->
            Log.e(TAG, "Error getting quiz documents: $exception")
        }
    }

    private fun scrollRecyclerView(direction: Int) {
        val layoutManager = recyclerView!!.layoutManager as LinearLayoutManager?
        if (layoutManager != null) {
            val currentPosition = layoutManager.findFirstVisibleItemPosition()
            val totalItemCount = layoutManager.itemCount

            val newPosition = currentPosition + direction

            if (newPosition >= 0 && newPosition < totalItemCount) {
                recyclerView!!.smoothScrollToPosition(newPosition)

                if (newPosition == totalItemCount - 1) {
                    // Last item is on the screen
                    Toast.makeText(context, "Last item is on the screen!", Toast.LENGTH_SHORT).show()
                    btn_submit.visibility = View.VISIBLE
                }
            }
        }
    }


    companion object {
        fun newInstance(param1: String?, param2: String?): QuizFragment {
            val fragment = QuizFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    //real time quizz
    fun getQuiz() {
        val firestore = FirebaseFirestore.getInstance()
        val collectionPath = "/Exams/CEReGwH8PMG8zXVQqP3B/ComputerScience/Ay8RWesFncvEBVmuIAx0/Questions"
        val collectionRef = firestore.collection(collectionPath)

        collectionRef.get().addOnSuccessListener { querySnapshot ->
            val quizList = mutableListOf<QuizModel>()

            for (document in querySnapshot) {
                val quiz = document.toObject(QuizModel::class.java)
                quizList.add(quiz)
            }

            val quizAdapter = QuizAdapter(quizList)
            recyclerView!!.adapter = quizAdapter
            collectAndStoreDocuments(quizList)

        }.addOnFailureListener { exception ->
            Log.e(TAG, "Error getting quiz documents: $exception")
        }
    }

    fun getMissedQuiz(no_of_quiz : Int) {
        val firestore = FirebaseFirestore.getInstance()
        val collectionPath = "/users/G897SE6IwSfSgpqiISIGNCBmSrI3/history"
        val collectionRef = firestore.collection(collectionPath)

        collectionRef.limit(no_of_quiz.toLong())
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val quizList = mutableListOf<QuizModel>()

                    for (document in querySnapshot) {
                        val quiz = document.toObject(QuizModel::class.java)
                        quizList.add(quiz)
                    }

                    val quizAdapter = QuizAdapter(quizList)
                    recyclerView!!.adapter = quizAdapter
                    collectAndStoreDocuments(quizList)

                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting quiz documents: $exception")
                }
    }

    fun getRandomQuestions() {
        val firestore = FirebaseFirestore.getInstance()
        val collectionPath = "/Exams/CEReGwH8PMG8zXVQqP3B/ComputerScience/Ay8RWesFncvEBVmuIAx0/Questions"
        val collectionRef = firestore.collection(collectionPath)

        collectionRef.get().addOnSuccessListener { querySnapshot ->
            val allQuestions = querySnapshot.toObjects(QuizModel::class.java)

            val shuffledQuestions = allQuestions.shuffled()
            val randomQuestions = shuffledQuestions.take(10)

            val quizAdapter = QuizAdapter(randomQuestions)
            recyclerView!!.adapter = quizAdapter
            collectAndStoreDocuments(randomQuestions)
        }.addOnFailureListener { exception ->
            Log.e(TAG, "Error getting quiz documents: $exception")
        }
    }


    fun startTimer(totalTimeMillis: Long) {
        countdownTimer = object : CountDownTimer(totalTimeMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val timeRemaining = millisUntilFinished / 1000

                tv_timer.text = formatTime(timeRemaining)

                if (timeRemaining <= 60) {
                    tv_timer.setTextColor(Color.RED)
                } else {
                    tv_timer.setTextColor(Color.GREEN)
                }
            }

            override fun onFinish() {
                tv_timer.text = "Time's Up!"
                tv_timer.setTextColor(Color.RED)
                rightArrow?.isClickable = false
                leftArrow?.isClickable = false
                recyclerView?.setOnTouchListener { _, _ -> true }

                // Freeze the screen or perform necessary actions
                // ...
            }
        }

        countdownTimer.start()
    }

    fun stopTimer() {
        if (::countdownTimer.isInitialized) {
            countdownTimer.cancel()
        }
    }

    private fun formatTime(seconds: Long): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }

    fun updateExistingDocuments() {
        val firestore = FirebaseFirestore.getInstance()
        val collectionPath = "/Exams/CEReGwH8PMG8zXVQqP3B/ComputerScience/Ay8RWesFncvEBVmuIAx0/Questions"
        val collectionRef = firestore.collection(collectionPath)

        collectionRef.get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot.documents) {
                val quizId = document.id
                // Update the document with the document ID field
                document.reference.update("questionId", quizId)
                        .addOnSuccessListener {
                            Log.d(TAG, "Document updated with ID: $quizId")
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "Error updating document: $e")
                        }
            }
        }.addOnFailureListener { exception ->
            Log.e(TAG, "Error getting documents: $exception")
        }


    }

    fun collectAndStoreDocuments(quizList: List<QuizModel>) {
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val user_id: String? = firebaseAuth.currentUser?.uid

        val firestore = FirebaseFirestore.getInstance()

        val destinationCollectionRef = firestore.collection("/users/$user_id/history/")

        for (quiz in quizList) {
            // Modify the values of attempted, correct, and flag fields
            quiz.attempted = false
            quiz.correct = false
            quiz.flagged = false

            val documentId = quiz.questionId

            if (documentId != null) {
                destinationCollectionRef.document(documentId).set(quiz)
                        .addOnSuccessListener {
                            Log.d(TAG, "Quiz document added at path: /users/history/questions/$documentId")
                        }
                        .addOnFailureListener { exception ->
                            Log.e(TAG, "Error adding quiz document at path: /users/history/questions/$documentId, $exception")
                        }
            }
        }
    }

}