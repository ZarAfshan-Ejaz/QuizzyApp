package com.codingstuff.quizzyapp.ui.dialogs

import android.app.AlertDialog
import android.content.ContentValues
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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codingstuff.quizzyapp.Adapter.DomainsAdapter
import com.codingstuff.quizzyapp.Adapter.QuizAdapter
import com.codingstuff.quizzyapp.Model.DomainModel
import com.codingstuff.quizzyapp.Model.QuizModel
import com.codingstuff.quizzyapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class BuildYourOwnQuestionDialog : Fragment() {
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val user_id: String? = firebaseAuth.currentUser?.uid
    val firestore = FirebaseFirestore.getInstance()
    var dom_filter = ""
    var domainsList: MutableList<DomainModel> = mutableListOf()
    var dom_filteredList: MutableList<DomainModel> = mutableListOf()
    private var listenerRegistration_filter: ListenerRegistration? = null

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
    var quizList_answered = mutableListOf<QuizModel>()
    var quizList_flagged = mutableListOf<QuizModel>()
    var quizList_missed = mutableListOf<QuizModel>()
    var finalCombinationList = mutableListOf<QuizModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val builder = AlertDialog.Builder(requireActivity())
        val view = inflater.inflate(R.layout.build_your_own_questions_dialog, null)
        //fetch_domains_list()
        init(view)
        // Set up other components and listeners as needed
        builder.setView(view)
        fetch_New_Quiz()
        copyDocumentIdsFromCollectionAtoB()
        fetchSpecificQuiz("flagged", "true", tv_flagged_ques)
        fetchSpecificQuiz("correct", "false", tv_missed_ques)
        fetchSpecificQuiz("attempted", "true", tv_answered_ques)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

    }

    private fun init(view: View) {
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

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_all_sub!!.setLayoutManager(layoutManager)


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


            if (finalCombinationList.isNotEmpty()){
                val bundle = Bundle()
                bundle.putString("QUIZ_CAT", "YOUR_OWN_QUIZ")
                //  bundle.putInt("NO_Of_Quiz", no_of_quiz)
                bundle.putSerializable("FINAL_COMBINATION_LIST", ArrayList(finalCombinationList))

                navController!!.navigate(
                    R.id.action_buildYourOwnQuestionDialog_to_quizFragment2,
                    bundle
                )
            }else{
                Toast.makeText(
                    context,
                    "no quiz : $finalCombinationList",
                    Toast.LENGTH_LONG
                ).show()

            }

        }


        cb_new_ques.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val value = tv_new_ques.text.toString()
                str_cb_new = value
                Toast.makeText(requireContext(), str_cb_new, Toast.LENGTH_LONG).show()

            } else {
                // If the checkbox is unchecked, you can handle it here if needed
            }
        }
        cb_missed_ques.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val value = tv_missed_ques.text.toString()
                str_cb_miss = value
                Toast.makeText(requireContext(), str_cb_miss, Toast.LENGTH_LONG).show()
            } else {
                // If the checkbox is unchecked, you can handle it here if needed
            }
        }
        cb_answered_ques.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val value = tv_answered_ques.text.toString()
                str_cb_ans = value
                Toast.makeText(requireContext(), str_cb_ans, Toast.LENGTH_LONG).show()
            } else {
                // If the checkbox is unchecked, you can handle it here if needed
            }
        }
        cb_flagged_ques.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val value = tv_flagged_ques.text.toString()
                str_cb_flagg = value
                Toast.makeText(requireContext(), str_cb_flagg, Toast.LENGTH_LONG).show()

            } else {
                // If the checkbox is unchecked, you can handle it here if needed
            }
        }

    }


    fun fetch_New_Quiz() {
        val quizList_A = mutableListOf<QuizModel>()

        // Fetch document IDs for quizList_A
        val collectionRef_A = firestore.collection("/users/$user_id/history")
        collectionRef_A.get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot.documents) {
                val documentId = document.id
                // Skip if the document ID is already present in quizList_A
                if (quizList_A.none { it.questionId == documentId }) {
                    val quizModel = QuizModel(questionId = documentId)
                    quizList_A.add(quizModel)
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
        val uidd = firebaseAuth.uid


        val newQuizSize = quizList.size
        tv_new_ques.text = newQuizSize.toString()
        /* tv_flagged_ques.text = getFlaggedQuizzes("flagged", true)
         tv_missed_ques.text = getSpecificQuizzes("attempted", false)
         tv_answered_ques.text = getSpecificQuizzes("attempted", true)*/
    }

    fun getSpecificQuizzes(key: String, value: Boolean): String {

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

    fun getQuiz(): Int {
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
            if (quiz.attempted == "true") {
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
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.byoq_subject_item, parent, false)
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

    private fun fetchSpecificQuiz(
        key: String,
        value: String,
        textView: TextView?,
    ) {
        var  quizList: MutableList<QuizModel> = mutableListOf()
        val firestore = FirebaseFirestore.getInstance()
        val collectionRef = firestore.collection("/users/$user_id/history")

        collectionRef.whereEqualTo(key, value)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val quizModel = document.toObject(QuizModel::class.java)
                    quizList.add(quizModel!!)
                }
                when (key){
                    "flagged" ->{
                        quizList_flagged = quizList
                    }
                    "correct" ->{
                        quizList_missed = quizList
                    }
                    "attempted" ->{
                        quizList_answered = quizList
                    }
                }

                // Perform further operations with the quizList containing the documents
                Toast.makeText(requireContext(), quizList.size.toString(), Toast.LENGTH_LONG).show()
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
        fetch_filtered_domains(quizList_combination)
/*
        if (quizList_combination.size < no_of_quiz.toInt()) {
            Toast.makeText(requireContext(), "plz Select less quizzes", Toast.LENGTH_SHORT).show()
        } else {
            finalCombinationList =
                quizList_combination.take(no_of_quiz.toInt()) as MutableList<QuizModel>
        }
*/

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

    fun copyDocumentIdsFromCollectionAtoB() {
        val firestore = FirebaseFirestore.getInstance()
        val collectionARef = firestore.collection("/Exams/computer_science/Domains/")

        collectionARef.get()
            .addOnSuccessListener { querySnapshot ->
                val batch = firestore.batch()

                for (document in querySnapshot.documents) {
                    val docId = document.id
                    val docRefInCollectionB =
                        firestore.collection("/users/$user_id/temp_dom_filter").document(docId)
                    batch.set(
                        docRefInCollectionB,
                        HashMap<String, Any>()
                    ) // Empty HashMap to create a document without any fields
                }

                batch.commit()
                    .addOnSuccessListener {
                        // The document IDs have been copied successfully
                        Log.d("Firestore", "Document IDs copied from CollectionA to CollectionB")
                    }
                    .addOnFailureListener { exception ->
                        // Handle the error
                        Log.e("Firestore", "Error copying document IDs: $exception")
                    }
            }
            .addOnFailureListener { exception ->
                // Handle the error
                Log.e("Firestore", "Error fetching documents from CollectionA: $exception")
            }
    }

    fun fetch_domains_list() {

        val collectionRef = firestore.collection("/Exams/computer_science/Domains")
        collectionRef
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val domModel = document.toObject(DomainModel::class.java)
                    domainsList.add(domModel!!)
                }
                val domAdapter = DomainsAdapter()
                domAdapter.setDomListModels(domainsList)
                rv_all_sub!!.adapter = domAdapter
            }
            .addOnFailureListener { exception ->
                // Handle the error
                Log.e(ContentValues.TAG, "Error fetching documents: $exception")
            }


    }
    fun fetch_filtered_domains(list : MutableList<QuizModel> ) {
        var filteredList = mutableListOf<QuizModel>()

        val collectionRef = firestore.collection("/users/$user_id/temp_dom_filter")
        collectionRef
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val documentId = document.id
                    val domModel = DomainModel(domainId = documentId)

                    filteredList = list.filter { quizModel ->
                        quizModel.domain_name != "$documentId"
                    }.toMutableList()


                    dom_filteredList.add(domModel!!)
                }
                val no_of_quiz = tv_num_of_ques.text.toString()

                if (filteredList.size < no_of_quiz.toInt()) {
                    Toast.makeText(requireContext(), "plz Select less quizzes", Toast.LENGTH_SHORT).show()
                } else {
                    finalCombinationList =
                        filteredList.take(no_of_quiz.toInt()) as MutableList<QuizModel>
                }
            }
            .addOnFailureListener { exception ->
                // Handle the error
                Log.e(ContentValues.TAG, "Error fetching documents: $exception")
            }


    }
    override fun onStop() {
        super.onStop()
        stopFirestoreListener()
    }
    override fun onStart() {
        super.onStart()
        startFirestoreListener()
    }
    private fun stopFirestoreListener() {
        listenerRegistration_filter?.remove()

        listenerRegistration_filter = null
        }
    private fun startFirestoreListener() {
        refreshDialog()
    }

    private fun refreshDialog() {
        fetch_domains_list()

    }

}
