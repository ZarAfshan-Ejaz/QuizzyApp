package com.codingstuff.quizzyapp.ui.dialogs

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.codingstuff.quizzyapp.Model.QuizModel
import com.codingstuff.quizzyapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BuildYourOwnQuestionDialog : Fragment() {
    lateinit var tv_num_of_ques: TextView
    lateinit var tv_new_ques: TextView
    lateinit var tv_missed_ques: TextView
    lateinit var tv_answered_ques: TextView
    lateinit var tv_flagged_ques: TextView
    lateinit var cb_new_ques: CheckBox
    lateinit var cb_missed_ques: CheckBox
    lateinit var cb_answered_ques: CheckBox
    lateinit var cb_flagged_ques: CheckBox
    lateinit var rb_after_ques: RadioButton
    lateinit var rb_after_submit: RadioButton
    lateinit var rb_tab_ans: RadioButton
    lateinit var rv_all_sub: RecyclerView
    lateinit var rb_tab_check_ans: RadioButton
    lateinit var seekBar: SeekBar
    var str_cb_new: String = ""
    var str_cb_ans: String = ""
    var str_cb_miss: String = ""
    var str_cb_flagg: String = ""
    private var navController: NavController? = null
    val quizList_new = mutableListOf<QuizModel>()
    val quizList_answered = mutableListOf<QuizModel>()
    val quizList_flagged = mutableListOf<QuizModel>()
    val quizList_missed = mutableListOf<QuizModel>()
    var finalCombinationList = mutableListOf<QuizModel>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         super.onCreateView(inflater, container, savedInstanceState)
        val builder = AlertDialog.Builder(requireActivity())
        val view = inflater.inflate(R.layout.build_your_own_questions_dialog, null)
        init(view)

        // Set up other components and listeners as needed
        builder.setView(view)
        fetch_New_Quiz()
        fetchSpecificQuiz("flagged", true,tv_flagged_ques, quizList_flagged)
        fetchSpecificQuiz("attempted", false, tv_missed_ques,quizList_missed)
        fetchSpecificQuiz("attempted", true,tv_answered_ques, quizList_answered)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

    }
private fun init(view: View){
    tv_num_of_ques = view.findViewById(R.id.tv_num_of_ques_b)
    tv_new_ques = view.findViewById(R.id.tv_new_ques)
    tv_missed_ques = view.findViewById(R.id.tv_missed_ques)
    tv_answered_ques = view.findViewById(R.id.tv_answered_ques)
    tv_flagged_ques = view.findViewById(R.id.tv_flagged_ques)
    cb_new_ques = view.findViewById(R.id.cb_new_ques)
    cb_missed_ques = view.findViewById(R.id.cb_missed_ques)
    cb_answered_ques = view.findViewById(R.id.cb_answered_ques)
    cb_flagged_ques = view.findViewById(R.id.cb_flagged_ques)
    rb_after_ques = view.findViewById(R.id.rb_after_ques)
    rb_after_submit = view.findViewById(R.id.rb_after_submit)
    rb_tab_ans = view.findViewById(R.id.rb_tab_ans)
    rb_tab_check_ans = view.findViewById(R.id.rb_tab_check_ans)
    seekBar = view.findViewById(R.id.seekBar)
    rv_all_sub = view.findViewById(R.id.rv_all_sub)
    val startButton = view.findViewById<Button>(R.id.btn_start_b)
    val img_close = view.findViewById<ImageView>(R.id.img_close_b)

    img_close.setOnClickListener(View.OnClickListener {
        Navigation.findNavController(requireActivity(), R.id.nav_graph).popBackStack()
    })

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Update tv_num_of_ques with the new progress value
                tv_num_of_ques.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        startButton.setOnClickListener {
            createCombinationList()

            val no_of_quiz = seekBar.progress
            val bundle = Bundle()
            bundle.putString("QUIZ_CAT", "your_owen_quiz")
          //  bundle.putInt("NO_Of_Quiz", no_of_quiz)
            bundle.putSerializable("FINAL_COMBINATION_LIST", ArrayList(finalCombinationList))

            navController!!.navigate(R.id.action_buildYourOwnQuestionDialog_to_quizFragment2,bundle)

        }


    cb_new_ques.setOnCheckedChangeListener { _, isChecked ->
        if (isChecked) {
            val value = tv_new_ques.text.toString()
            str_cb_new = value
            Toast.makeText(requireContext(),str_cb_new,Toast.LENGTH_LONG).show()

        } else {
            // If the checkbox is unchecked, you can handle it here if needed
        }
    }
    cb_missed_ques.setOnCheckedChangeListener { _, isChecked ->
        if (isChecked) {
            val value = tv_missed_ques.text.toString()
            str_cb_miss = value
            Toast.makeText(requireContext(),str_cb_miss,Toast.LENGTH_LONG).show()
        } else {
            // If the checkbox is unchecked, you can handle it here if needed
        }
    }
    cb_answered_ques.setOnCheckedChangeListener { _, isChecked ->
        if (isChecked) {
            val value = tv_answered_ques.text.toString()
            str_cb_ans = value
            Toast.makeText(requireContext(),str_cb_ans,Toast.LENGTH_LONG).show()
        } else {
            // If the checkbox is unchecked, you can handle it here if needed
        }
    }
    cb_flagged_ques.setOnCheckedChangeListener { _, isChecked ->
        if (isChecked) {
            val value = tv_flagged_ques.text.toString()
            str_cb_flagg = value
            Toast.makeText(requireContext(),str_cb_flagg,Toast.LENGTH_LONG).show()

        } else {
            // If the checkbox is unchecked, you can handle it here if needed
        }
    }

}

    fun fetch_New_Quiz() {
        val quizList_A = mutableListOf<QuizModel>()
        val firestore = FirebaseFirestore.getInstance()

        // Fetch document IDs for quizList_A
        val collectionRef_A = firestore.collection("/users/G897SE6IwSfSgpqiISIGNCBmSrI3/history")
        collectionRef_A.get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot.documents) {
                val documentId = document.id
                // Skip if the document ID is already present in quizList_A
                if (quizList_A.none { it.questionId == documentId }) {
                    val quizModel = QuizModel(questionId = documentId)
                    quizList_A.add(quizModel)
                }
            }
        }.addOnFailureListener { exception ->
            Log.e(TAG, "Error getting documents for path_A: $exception")
        }

        // Fetch document IDs for quizList_B
        val collectionRef_B = firestore.collection("/Exams/ComputerScience/Questions")
        collectionRef_B.get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot.documents) {
                val documentId = document.id
                // Skip if the document ID is already present in quizList_A
                if (quizList_A.none { it.questionId == documentId }) {
                    val quizModel = QuizModel(questionId = documentId)
                    quizList_new.add(quizModel)
                }
            }
            processQuizListA(quizList_new)
        }.addOnFailureListener { exception ->
            Log.e(TAG, "Error getting documents for path_B: $exception")
        }
    }

    fun processQuizListA(quizList: MutableList<QuizModel>) {


        val newQuizSize = quizList.size
        tv_new_ques.text = newQuizSize.toString()
        /* tv_flagged_ques.text = getFlaggedQuizzes("flagged", true)
         tv_missed_ques.text = getSpecificQuizzes("attempted", false)
         tv_answered_ques.text = getSpecificQuizzes("attempted", true)*/
    }

    fun getSpecificQuizzes(key: String, value: Boolean): String {
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val user_id: String? = firebaseAuth.currentUser?.uid

        val firestore = FirebaseFirestore.getInstance()
        val collectionPath = "/users/$user_id/history"
        val collectionRef = firestore.collection(collectionPath)
        val flaggedQuizList = mutableListOf<QuizModel>()

        collectionRef.whereEqualTo(key, value)
                .get()
                .addOnSuccessListener { querySnapshot ->

                    for (document in querySnapshot) {
                        val quiz = document.toObject(QuizModel::class.java)
                        flaggedQuizList.add(quiz)
                    }
                }.addOnFailureListener { exception ->
                }
        return flaggedQuizList.size.toString()
    }

    fun getFlaggedQuizzes(key: String, value: Boolean): List<QuizModel> {
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val user_id: String? = firebaseAuth.currentUser?.uid

        val firestore = FirebaseFirestore.getInstance()
        val collectionPath = "/users/$user_id/history"
        val collectionRef = firestore.collection(collectionPath)
        val flaggedQuizList = mutableListOf<QuizModel>()

        collectionRef.whereEqualTo(key, value)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        val documentId = document.id
                        val quizModel = document.toObject(QuizModel::class.java)
                        flaggedQuizList.add(quizModel!!)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting flagged documents: $exception")
                }

        return flaggedQuizList
    }

    fun getFlaggedQuizzes(key: String, value: Boolean, callback: (String) -> Unit) {
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val user_id: String? = firebaseAuth.currentUser?.uid

        val firestore = FirebaseFirestore.getInstance()
        val collectionPath = "/users/$user_id/history"
        val collectionRef = firestore.collection(collectionPath)
        val flaggedQuizList = mutableListOf<QuizModel>()

        collectionRef.whereEqualTo(key, value)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        val documentId = document.id
                        val quizModel = QuizModel(questionId = documentId)
//                        val quiz = document.toObject(QuizModel::class.java)
                        flaggedQuizList.add(quizModel)
                    }
                    val size = flaggedQuizList.size.toString()
                    callback(size)
                }
                .addOnFailureListener { exception ->
                    // Handle the failure
                    callback("0")
                }
    }

    fun fetchAttemptedQuizzes(key: String, value: Boolean): String {
        val attemptedQuizList = mutableListOf<QuizModel>()

        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val user_id: String? = firebaseAuth.currentUser?.uid

        val firestore = FirebaseFirestore.getInstance()
        val collectionPath = "/users/$user_id/history/"
        val collectionRef = firestore.collection(collectionPath)

        collectionRef.whereEqualTo(key, value)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        val documentId = document.id
                        val quizModel = QuizModel(questionId = documentId)
                        attemptedQuizList.add(quizModel)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting attempted documents: $exception")
                }
        return attemptedQuizList.size.toString()
    }
    fun getQuiz() : Int{
        val quizList = mutableListOf<QuizModel>()
        val attemptedList = mutableListOf<QuizModel>()

        val firestore = FirebaseFirestore.getInstance()
        val collectionPath = "/users/$/history"
        val collectionRef = firestore.collection(collectionPath)

        collectionRef.get().addOnSuccessListener { querySnapshot ->

            for (document in querySnapshot) {
                val quiz = document.toObject(QuizModel::class.java)
                quizList.add(quiz)
            }

        }.addOnFailureListener { exception ->
            Log.e(TAG, "Error getting quiz documents: $exception")
        }
        for (quiz in quizList) {
            if (quiz.attempted == true) {
                attemptedList.add(quiz)
            }
        }

        return attemptedList.size
    }

    inner class AllSubjectAdapter : RecyclerView.Adapter<AllSubjectAdapter.ViewHolder>() {

        // ViewHolder class for the adapter
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            // ViewHolder implementation
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            // Create and return a new ViewHolder
            val view = LayoutInflater.from(parent.context).inflate(R.layout.byoq_subject_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // Bind data to the ViewHolder
        }

        override fun getItemCount(): Int {
            // Return the item count
            return 0
        }
    }

    private fun fetchSpecificQuiz(key: String, value: Boolean, textView: TextView?, quizList : MutableList<QuizModel>) {
        val firestore = FirebaseFirestore.getInstance()
        val collectionRef = firestore.collection("/users/G897SE6IwSfSgpqiISIGNCBmSrI3/history")

        collectionRef.whereEqualTo(key, value)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        val quizModel = document.toObject(QuizModel::class.java)
                        quizList.add(quizModel!!)
                    }
                    // Perform further operations with the quizList containing the documents
                    Toast.makeText(requireContext(),quizList.size.toString(),Toast.LENGTH_LONG).show()
                    textView?.text = quizList.size.toString()
                }
                .addOnFailureListener { exception ->
                    // Handle the error
                    Log.e(TAG, "Error fetching documents: $exception")
                }

    }
    fun createCombinationList() {

        val quizList_combination = mutableListOf<QuizModel>()

       /* quizList_combination.addAll(getRandomItems(quizList_new, 3))
        quizList_combination.addAll(getRandomItems(quizList_answered, 3))
        quizList_combination.addAll(getRandomItems(quizList_flagged, 2))
        quizList_combination.addAll(getRandomItems(quizList_missed, 2))*/
        quizList_combination.addAll(quizList_new)
        quizList_combination.addAll(quizList_answered)
        quizList_combination.addAll(quizList_flagged)
        quizList_combination.addAll(quizList_missed)
        quizList_combination.shuffle()

        // Take the first 10 items from the shuffled combination list
        val no_of_quiz = tv_num_of_ques.text.toString()

        if (quizList_combination.size <no_of_quiz.toInt()){
            Toast.makeText(requireContext(),"plz Select less quizzes", Toast.LENGTH_SHORT).show()
        }else{
            finalCombinationList = quizList_combination.take(no_of_quiz.toInt()) as MutableList<QuizModel>

        }


        // Use the finalCombinationList for further operations
    }

    fun getRandomItems(list: List<QuizModel>, count: Int): List<QuizModel> {
        val randomItems = mutableListOf<QuizModel>()
        val shuffledList = list.shuffled()
        val itemsToTake = minOf(count, list.size)
        for (i in 0 until itemsToTake) {
            randomItems.add(shuffledList[i])
        }
        return randomItems
    }

}

fun filterAttemptedQuizzes(key: String, value: Boolean): String {
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val user_id: String? = firebaseAuth.currentUser?.uid

    val firestore = FirebaseFirestore.getInstance()
    val collectionPath = "/users/$user_id/history"
    val collectionRef = firestore.collection(collectionPath)

    val allQuizList = mutableListOf<QuizModel>()
    val attemptedQuizList = mutableListOf<QuizModel>()


    collectionRef.get().addOnSuccessListener { querySnapshot ->
        for (document in querySnapshot.documents) {
            val documentId = document.id
            // Skip if the document ID is already present in quizList_A
            if (allQuizList.none { it.questionId == documentId }) {
                val quizModel = QuizModel(questionId = documentId)
                allQuizList.add(quizModel)
            }
        }
    }.addOnFailureListener { exception ->
        Log.e(TAG, "Error getting documents for path_A: $exception")
    }
    attemptedQuizList.clear() // Clear the existing attempted quizzes

    for (quiz in allQuizList) {
        if (quiz.attempted == value) {
            attemptedQuizList.add(quiz)
        }
    }
    return attemptedQuizList.size.toString()
}

