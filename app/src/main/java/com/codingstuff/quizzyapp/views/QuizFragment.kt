package com.codingstuff.quizzyapp.views

import android.app.Dialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codingstuff.quizzyapp.Adapter.QuizAdapter
import com.codingstuff.quizzyapp.BaseActivity
import com.codingstuff.quizzyapp.Model.QuizModel
import com.codingstuff.quizzyapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class QuizFragment : Fragment() {
    private var listenerRegistration_atmpt_quiz: ListenerRegistration? = null

    var no_of_attempted_quiz : Int = 0
    private var navController: NavController? = null

    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val user_id: String? = firebaseAuth.currentUser?.uid
    val firestore = FirebaseFirestore.getInstance()

    var no_Of_Quiz : Int? = 0
    private lateinit var countdownTimer: CountDownTimer
    var quizList = mutableListOf<QuizModel>()
    var weakest_SUB_LIST: MutableList<QuizModel> = mutableListOf()

    private var recyclerView: RecyclerView? = null
    private var leftArrow: ImageButton? = null
    private var rightArrow: ImageButton? = null
    lateinit var tv_timer: TextView
    lateinit var btn_submit: Button
    var quiz_CAT: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_quiz2, container, false)
        val btnclose_quiz: ImageView = view.findViewById(R.id.img_close_quiz)

        recyclerView = view.findViewById(R.id.recyclerView)
        leftArrow = view.findViewById(R.id.leftArrow)
        rightArrow = view.findViewById(R.id.rightArrow)
        tv_timer = view.findViewById<TextView>(R.id.tv_timer)
        btn_submit = view.findViewById<Button>(R.id.btn_submit)


        val args = arguments
        val startTimer = args?.getBoolean("START_TIMER")
        val millisecondsSelected = args?.getLong("millisecondsSelected")
         no_Of_Quiz = args?.getInt("NO_Of_Quiz")
        quiz_CAT = args?.getString("QUIZ_CAT")
        val finalCombinationList = arguments?.getSerializable("FINAL_COMBINATION_LIST") as? ArrayList<QuizModel>
        val weakest_SUB_LIST = arguments?.getSerializable("WEAKEST_SUB_LIST") as? ArrayList<QuizModel>
        val missed_quiz_list = arguments?.getSerializable("MISSED_QUIZ_LIST") as? ArrayList<QuizModel>
        val qotd_quiz = arguments?.getSerializable("QotD") as? ArrayList<QuizModel>
        val quick_10_quiz_list = arguments?.getSerializable("QUICK_10_QUIZ_LIST") as? ArrayList<QuizModel>

        if (quiz_CAT == "WEAKEST_SUBJECT") {
            tv_timer.visibility = View.GONE
            removeCollection()
            quizList.clear()
            if (weakest_SUB_LIST != null) {
                quizList.clear()
                quizList =weakest_SUB_LIST

                show_btn_submit()
            }
        }
        if (quiz_CAT == "TIMED_QUIZ") {
            if (millisecondsSelected != null) {
                startTimer(millisecondsSelected)
                removeCollection()
                quizList.clear()
                getQuiz()
                show_btn_submit()
            }
        }
        if (quiz_CAT == "YOUR_OWN_QUIZ") {
            tv_timer.visibility = View.GONE

            if (finalCombinationList != null) {
                removeCollection()
                quizList.clear()
                quizList = finalCombinationList
                show_btn_submit()
            }
        }
        if (quiz_CAT == "MISSED_QUIZ") {
            tv_timer?.visibility = View.GONE
            if (missed_quiz_list != null) {
                removeCollection()
                quizList.clear()
                quizList=missed_quiz_list
                show_btn_submit()
            }
        }
        if (quiz_CAT == "QUICK_10_QUIZ") {
            tv_timer?.visibility = View.GONE
            if (quick_10_quiz_list != null) {
                removeCollection()
                quizList.clear()
                quizList=quick_10_quiz_list
                show_btn_submit()
            }

        }
        if (quiz_CAT == "QotD") {
            rightArrow?.visibility = View.GONE
            leftArrow?.visibility = View.GONE
            tv_timer?.visibility = View.GONE
            btn_submit?.visibility = View.VISIBLE
            quizList.clear()
            //question_of_the_day()
            if (qotd_quiz != null) {
                removeCollection()
                quizList.clear()
                quizList= qotd_quiz
                show_btn_submit()
            }
        }
        btnclose_quiz.setOnClickListener(View.OnClickListener {

            no_of_attempted_quiz()
            openDialog(no_of_attempted_quiz, R.layout.quit_quiz_dialog)

        })
        btn_submit.setOnClickListener(View.OnClickListener {
            //updateSubmittedQuiz(quiz_CAT)
            moveToNewPath()

        })
        leftArrow!!.setOnClickListener(View.OnClickListener { scrollRecyclerView(-1) })
        rightArrow!!.setOnClickListener(View.OnClickListener { scrollRecyclerView(1) })
        // Create a LinearLayoutManager and set it as the layout manager for the RecyclerView
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView!!.setLayoutManager(layoutManager)
        return view
    }
    private fun question_of_the_day() {
        val collectionPath = "/Exams/computer_science/Questions"
        val collectionRef = firestore.collection(collectionPath)

        collectionRef.get().addOnSuccessListener { querySnapshot ->
            val allQuestions = querySnapshot.toObjects(QuizModel::class.java)

            val shuffledQuestions = allQuestions.shuffled()
            val randomQuestions = shuffledQuestions.take(1)
            quizList = randomQuestions as MutableList<QuizModel>
            val quizAdapter = QuizAdapter(quizList, quiz_CAT, requireContext())
            recyclerView!!.adapter = quizAdapter

        }.addOnFailureListener { exception ->
            Log.e(TAG, "Error getting quiz documents: $exception")
        }
    }
/*
//fetch quiz randomly according to the date
    fun fetch_New_QotD_Quiz() {
        val quizList_A = mutableListOf<QuizModel>()
        val new_quiz_list = mutableListOf<QuizModel>()

        // Fetch document IDs for quizList_A
        val collectionRef_A = firestore.collection("/users/$user_id/history_of_QotD")
        collectionRef_A.whereEqualTo("correct","true").get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot.documents) {
                val documentId = document.id
                // Skip if the document ID is already present in quizList_A
                if (quizList_A.none { it.questionId == documentId }) {
                    //val quizModel = QuizModel(questionId = documentId)
                    //quizList_A.add(quizModel)
                    val quiz = document.toObject(QuizModel::class.java)
                    quizList_A.add(quiz!!)
                }
            }
        }.addOnFailureListener { exception ->
            Log.e(TAG, "Error getting documents for path_A: $exception")
        }

        // Fetch document IDs for quizList_B
        val collectionRef_B = firestore.collection("/Exams/computer_science/Questions")
        collectionRef_B.get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot.documents) {
                val documentId = document.id
                // Skip if the document ID is already present in quizList_A
                if (quizList_A.none { it.questionId == documentId }) {
                   // val quizModel = QuizModel(questionId = documentId)
                    //new_quiz_list.add(quizModel)
                    val quiz = document.toObject(QuizModel::class.java)
                    new_quiz_list.add(quiz!!)
                }
            }
            val shuffledQuestions = new_quiz_list.shuffled()
            val randomQuestions = shuffledQuestions.take(1)
            quizList = randomQuestions as MutableList<QuizModel>
            val quizAdapter = QuizAdapter(quizList, quiz_CAT, requireContext())
            recyclerView!!.adapter = quizAdapter
            Toast.makeText(context, "ID: ${quizList.get(0).questionId}", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener { exception ->
            Log.e(TAG, "Error getting documents for path_B: $exception")
        }
    }
*/

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


    fun show_btn_submit(){
        if (quizList.size == 1){
            btn_submit.visibility = View.VISIBLE
            leftArrow?.visibility = View.GONE
            rightArrow?.visibility = View.GONE
        }
        val quizAdapter = QuizAdapter(quizList, quiz_CAT, requireContext())
        recyclerView!!.adapter = quizAdapter
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
        val collectionPath = "/Exams/computer_science/Questions"
        val collectionRef = firestore.collection(collectionPath)

        collectionRef.get().addOnSuccessListener { querySnapshot ->

            for (document in querySnapshot) {
                val quiz = document.toObject(QuizModel::class.java)
                quizList.add(quiz)
            }

            val quizAdapter = QuizAdapter(quizList, quiz_CAT, requireContext())
            recyclerView!!.adapter = quizAdapter

        }.addOnFailureListener { exception ->
            Log.e(TAG, "Error getting quiz documents: $exception")
        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

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
                quizList.clear()
                quizList = (requireContext() as BaseActivity).attemptedTimeQuiz
                btn_submit.visibility = View.VISIBLE

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
        val collectionPath = "/Exams/computer_science/Questions"
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


        val destinationCollectionRef = firestore.collection("/users/$user_id/history/")

        for (quiz in quizList) {
            // Modify the values of attempted, correct, and flag fields
            quiz.attempted = ""
            quiz.correct = ""
            quiz.flagged = ""

            val documentId = quiz.questionId

            if (documentId != null) {
                destinationCollectionRef.document(documentId).set(quiz)
                        .addOnSuccessListener {
                            Log.d(TAG, "Quiz document added / $documentId")
                        }
                        .addOnFailureListener { exception ->
                            Log.e(TAG, "Error adding quiz document at path: " +
                                    "$documentId, $exception")
                        }
            }
        }
    }
    private fun openDialog(quiz_attempted : Int ,dialogId: Int) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogId)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window!!.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = layoutParams

        if (dialogId == (R.layout.quit_quiz_dialog)) {
            val btnQuit: Button = dialog.findViewById(R.id.btn_quit)
            val btnSubmitQuiz: Button = dialog.findViewById(R.id.btn_submit_quiz)
            val btnContinue: Button = dialog.findViewById(R.id.btn_continue)
            val tv_progress: TextView = dialog.findViewById(R.id.tv_progress)

            tv_progress.text = "You have answered $no_of_attempted_quiz of ${quizList.size} questions."
            if (no_of_attempted_quiz == 0){
                btnSubmitQuiz.visibility = View.GONE
            }else{
                btnSubmitQuiz.visibility = View.VISIBLE
                if (no_of_attempted_quiz>1){
                    btnSubmitQuiz.text = "Submit $no_of_attempted_quiz Questions"
                }else{
                    btnSubmitQuiz.text = "Submit $no_of_attempted_quiz Question"
                }
            }
            btnQuit.setOnClickListener {
                removeCollection()
                dialog.dismiss()
                navController!!.navigate(R.id.action_quizFragment_to_studyFragment)
            }
            btnSubmitQuiz.setOnClickListener {
                moveToNewPath()
                dialog.dismiss()
                navController!!.navigate(R.id.action_quizFragment_to_studyFragment)
            }
            btnContinue.setOnClickListener {
                // Only dismiss the dialog
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            no_of_attempted_quiz()
            openDialog(no_of_attempted_quiz, R.layout.quit_quiz_dialog)

            //  requireActivity().supportFragmentManager.popBackStack()
        }
    }
    private fun moveToNewPath() {

        val coll_ref_temp = firestore.collection("/users/$user_id/temp_history/")
        val coll_ref_his = firestore.collection("/users/$user_id/history/")

        coll_ref_temp.get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot.documents) {
                val documentData = document.toObject(QuizModel::class.java)
                val documentId = document.id
                coll_ref_his.document(documentId).set(documentData!!)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Quiz Submitted Successfully", Toast.LENGTH_SHORT).show()
                        removeCollection()
                    }
                    .addOnFailureListener { exception ->
                        Log.e(ContentValues.TAG, "Error duplicating document: $documentId", exception)
                    }
            }
        }
            .addOnFailureListener { exception ->
                Log.e(ContentValues.TAG, "Error fetching documents from path_A: $exception")
            }
    }
    private fun removeCollection() {
        val collRef = firestore.collection("/users/$user_id/temp_history/")

        collRef.get().addOnSuccessListener { querySnapshot ->
            val batch = collRef.firestore.batch()
            for (document in querySnapshot) {
                batch.delete(document.reference)
            }

            batch.commit()
                .addOnSuccessListener {
                    // All documents in the collection deleted successfully
                    collRef.firestore.runTransaction { transaction ->
                        collRef.parent?.let { it1 -> transaction.delete(it1) }
                    }
                        .addOnSuccessListener {
                            // Collection removed successfully
                            // You can perform any additional actions here after the collection removal
                            Log.d(ContentValues.TAG, "collection removed: ${collRef.path}")

                        }
                        .addOnFailureListener { exception ->
                            // Handle the error while removing the collection
                            Log.e(ContentValues.TAG, "Error removing collection: ${collRef.path}", exception)
                        }
                }
                .addOnFailureListener { exception ->
                    // Handle the error while deleting documents
                    Log.e(ContentValues.TAG, "Error deleting documents from collection: ${collRef.path}", exception)
                }
        }
            .addOnFailureListener { exception ->
                // Handle the error while fetching documents in the collection
                Log.e(ContentValues.TAG, "Error fetching documents from collection: ${collRef.path}", exception)
            }
    }
    fun no_of_attempted_quiz() {
        val collectionPath = "/users/$user_id/temp_history"
        val collectionRef = firestore.collection(collectionPath)
        listenerRegistration_atmpt_quiz = collectionRef
            .addSnapshotListener { documentSnapshot, e ->
                if (documentSnapshot != null) {
                    no_of_attempted_quiz = 0
                    for (document in documentSnapshot.documents) {
                        // Process each document here
                        val documentId = document.id
                        no_of_attempted_quiz++
                    }
                }
            }
    }
    private fun stopFirestoreListener() {
        listenerRegistration_atmpt_quiz?.remove()

        listenerRegistration_atmpt_quiz = null
    }
    private fun startFirestoreListener() {
        no_of_attempted_quiz()
    }
    override fun onStart() {
        super.onStart()
        startFirestoreListener()
    }

    override fun onStop() {
        super.onStop()
        stopFirestoreListener()
    }


}