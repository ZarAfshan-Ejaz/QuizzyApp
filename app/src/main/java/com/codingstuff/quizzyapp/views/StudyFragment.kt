package com.codingstuff.quizzyapp.views

import android.app.Dialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codingstuff.quizzyapp.Adapter.Calendar_QotD_Adapter
import com.codingstuff.quizzyapp.CalendarAdapter
import com.codingstuff.quizzyapp.Model.CalenderModel
import com.codingstuff.quizzyapp.Model.DomainModel
import com.codingstuff.quizzyapp.Model.QuizModel
import com.codingstuff.quizzyapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class StudyFragment : Fragment() {
     var no_of_streaks = 0
    lateinit var calendarAdapter: CalendarAdapter
    lateinit var calendar_QotD_Adapter: Calendar_QotD_Adapter
    private var listenerRegistration: ListenerRegistration? = null
    private var listenerRegistration_dom: ListenerRegistration? = null
    private var listenerRegistration_QotD_cal: ListenerRegistration? = null

    private var currentDate: Date = Date()
    var highlightedDates: MutableList<String> = mutableListOf()
    val dateList: MutableList<CalenderModel> = mutableListOf()
    val docsList: MutableList<String> = mutableListOf()
    val docs_QotD_List: MutableList<String> = mutableListOf()


    var domainId_d: String? = null
    var domain_name_d: String? = null
    var correct_d: String? = null
    var incorrect_d: String? = null
    var totalQuiz_d: String? = null
    var progress_percentage_d: String? = null

    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val user_id: String? = firebaseAuth.currentUser?.uid
    val firestore = FirebaseFirestore.getInstance()
    var smallestDomainModel: DomainModel? = DomainModel()
    var weakest_subject_nane: String? = null
    var quick_10_quizList: MutableList<QuizModel> = mutableListOf()
    var weakestQuizListTesting: MutableList<QuizModel> = mutableListOf()
    var domainsList: MutableList<DomainModel> = mutableListOf()
    var incor_quiz_weakest_sub_list: MutableList<QuizModel> = mutableListOf()

    var missedQuizList: MutableList<QuizModel> = mutableListOf()
    var qotd_quiz: MutableList<QuizModel> = mutableListOf()
    var no_of_Domains: Int = 0

    lateinit var tv_streak: TextView
    lateinit var tv_allStudying_btn: TextView
    lateinit var tv_QotD_btn: TextView
    lateinit var tv_studied_updates_QotD: TextView
    lateinit var img_studied_QotD: ImageView
    lateinit var btn_tv_hide_QotD: ImageView
    lateinit var btn_tv_show_QotD: ImageView
    lateinit var rv_QotD_calendarView: RecyclerView

    lateinit var tv_studied_updates: TextView
    lateinit var tv_greeting: TextView
    lateinit var add_quiz_to_db: TextView
    lateinit var tv_sub_greeting: TextView
    lateinit var btn_update_exiting_quiz: TextView
    lateinit var img_studied: ImageView
    lateinit var btn_tv_hide: ImageView
    lateinit var btn_tv_show: ImageView
    lateinit var rv_calendarView: RecyclerView
    private var navController: NavController? = null

    lateinit var ll_btn_qotd: LinearLayout
    lateinit var ll_btn_build_quiz: LinearLayout
    lateinit var ll_btn_missed_quiz: LinearLayout
    lateinit var ll_btn_timed_quiz: LinearLayout
    lateinit var ll_btn_weakest_subject_quiz: LinearLayout
    lateinit var ll_btn_quick_10_quiz: LinearLayout
    lateinit var ll_all_studying: LinearLayout
    lateinit var ll_QotD: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        refreshFragment()

        if (arguments != null) {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_study, container, false)
        refreshFragment()


        init(view)
        getGreetingMsg()
        return view
    }

    fun init(view: View) {
        ll_btn_qotd = view.findViewById<LinearLayout>(R.id.ll_btn_qotd)
        ll_btn_build_quiz = view.findViewById<LinearLayout>(R.id.ll_build_your_own_quiz)
        ll_btn_missed_quiz = view.findViewById<LinearLayout>(R.id.ll_btn_missed_quiz)
        ll_btn_timed_quiz = view.findViewById<LinearLayout>(R.id.ll_btn_timed_quiz)
        ll_btn_weakest_subject_quiz =
            view.findViewById<LinearLayout>(R.id.ll_btn_weakest_subject_quiz)
        ll_btn_quick_10_quiz = view.findViewById<LinearLayout>(R.id.ll_btn_quick_10_quiz)
//        val calendarView: CalendarView = view.findViewById<CalendarView>(R.id.calendar_view)
        rv_QotD_calendarView = view.findViewById<RecyclerView>(R.id.calendar_view_rv_QotD)
        btn_tv_hide_QotD = view.findViewById<ImageView>(R.id.btn_tv_hide_QotD)
        btn_tv_show_QotD = view.findViewById<ImageView>(R.id.btn_tv_show_QotD)
        tv_studied_updates_QotD = view.findViewById<TextView>(R.id.tv_studied_updates_QotD)

        tv_studied_updates = view.findViewById<TextView>(R.id.tv_studied_updates)
        tv_greeting = view.findViewById<TextView>(R.id.tv_greeting)
        rv_calendarView = view.findViewById<RecyclerView>(R.id.calendar_view_rv)
        btn_tv_hide = view.findViewById<ImageView>(R.id.btn_tv_hide)
        btn_tv_show = view.findViewById<ImageView>(R.id.btn_tv_show)

        tv_QotD_btn = view.findViewById<TextView>(R.id.tv_QotD_btn)
        ll_QotD = view.findViewById<LinearLayout>(R.id.ll_QotD)

        tv_allStudying_btn = view.findViewById<TextView>(R.id.tv_allStudying_btn)
        ll_all_studying = view.findViewById<LinearLayout>(R.id.ll_all_studying)

        add_quiz_to_db = view.findViewById<TextView>(R.id.tv_allStudying_btn)
        tv_sub_greeting = view.findViewById<TextView>(R.id.tv_sub_greeting)

        btn_update_exiting_quiz = view.findViewById<TextView>(R.id.btn_update_exiting_quiz)

        tv_streak = view.findViewById<TextView>(R.id.tv_streak)


        //no_of_streaks = findLastConsecutiveDates(docs_QotD_List)
        var msg: String
        if (no_of_streaks > 1) {
            msg = no_of_streaks.toString() + " days streak"
        } else{
            msg = no_of_streaks.toString() + " day streak"
        }
        tv_streak.text = msg

        tv_greeting.text = setGreetingMessage()
        rv_QotD_calendarView.layoutManager = GridLayoutManager(requireContext(), 7)
        calendar_QotD_Adapter = Calendar_QotD_Adapter(requireContext())
        rv_QotD_calendarView.adapter = calendar_QotD_Adapter

        rv_calendarView.layoutManager = GridLayoutManager(requireContext(), 7)
        calendarAdapter = CalendarAdapter(requireContext())
        rv_calendarView.adapter = calendarAdapter
        //   refreshFragment()
        add_quiz_to_db.setOnClickListener(View.OnClickListener {
            add_quiz()
        })
        btn_update_exiting_quiz.setOnClickListener(View.OnClickListener {
            //removeAnyField()
            // update_multiple_fields()
            add_quiz()
            //removeDocumentsWithNullQuestion()
            //moveToNewPath()
        })

        btn_tv_hide.setOnClickListener(View.OnClickListener {
            tv_studied_updates.visibility = View.GONE
            rv_calendarView.visibility = View.VISIBLE
            btn_tv_hide.visibility = View.GONE
            btn_tv_show.visibility = View.VISIBLE
        })
        btn_tv_show.setOnClickListener(View.OnClickListener {
            tv_studied_updates.visibility = View.VISIBLE
            rv_calendarView.visibility = View.GONE
            rv_calendarView.visibility = View.GONE
            btn_tv_show.visibility = View.GONE
            btn_tv_hide.visibility = View.VISIBLE

        })
        setupCalendarView()

        tv_QotD_btn.setOnClickListener {
            ll_all_studying.visibility = View.GONE
            ll_QotD.visibility = View.VISIBLE
            tv_QotD_btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_font))
            tv_allStudying_btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }
        tv_allStudying_btn.setOnClickListener {
            ll_all_studying.visibility = View.VISIBLE
            ll_QotD.visibility = View.GONE
            tv_allStudying_btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_font))
            tv_QotD_btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }
        ll_btn_qotd.setOnClickListener {
            is_QotD_attempted()
        }
        ll_btn_build_quiz.setOnClickListener {
            navController!!.navigate(R.id.action_studyFragment_to_buildYourOwnQuestionDialog)
        }
        ll_btn_missed_quiz.setOnClickListener {
            openDialog(R.layout.missed_questions_quiz_dialog)
        }
        ll_btn_timed_quiz.setOnClickListener {
            openDialog(R.layout.select_quiz_time_dialog)
        }
        ll_btn_weakest_subject_quiz.setOnClickListener {
            smallest_Dom()
            openDialog(R.layout.weakest_subject_quiz_dialog)
        }
        ll_btn_quick_10_quiz.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("QUIZ_CAT", "QUICK_10_QUIZ")
            bundle.putSerializable(
                "QUICK_10_QUIZ_LIST",
                ArrayList(quick_10_quizList)
            )
            if (quick_10_quizList.size == 10) {

                navController!!.navigate(R.id.action_studyFragment_to_quizFragment, bundle)
            } else {
                Toast.makeText(
                    context,
                    "No quiz available for now",
                    Toast.LENGTH_LONG
                ).show()

            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

    }

    private fun openDialog(dialogId: Int) {
        if (domainId_d != null)
            fetch_weakest_quizList(domainId_d!!.toInt())

        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogId)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window!!.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = layoutParams

        if (dialogId == (R.layout.select_quiz_time_dialog)) {
            val img_close = dialog.findViewById<ImageView>(R.id.img_close_t)
            val startButton = dialog.findViewById<Button>(R.id.btn_start)
            val seekBar = dialog.findViewById<SeekBar>(R.id.seekBar_t)
            val tv_num_of_ques_t = dialog.findViewById<TextView>(R.id.tv_num_of_ques_t)

            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    // Update tv_num_of_ques with the new progress value
                    tv_num_of_ques_t.text = progress.toString()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })
            startButton.setOnClickListener {
                val minutesSelected = seekBar.progress
                val millisecondsSelected = minutesSelected * 60 * 1000L
                val bundle = Bundle()
                bundle.putString("QUIZ_CAT", "TIMED_QUIZ")
                bundle.putLong("millisecondsSelected", millisecondsSelected)
                navController!!.navigate(R.id.action_studyFragment_to_quizFragment, bundle)
                dialog.dismiss()
            }
            img_close.setOnClickListener(View.OnClickListener { dialog.dismiss() })
        }
        if (dialogId == (R.layout.weakest_subject_quiz_dialog)) {
            val startButton = dialog.findViewById<Button>(R.id.btn_dismiss)
            val seekBar = dialog.findViewById<SeekBar>(R.id.seekBar_w)
            val tv_num_of_ques = dialog.findViewById<TextView>(R.id.tv_num_of_ques_w)
            val tv_max_w = dialog.findViewById<TextView>(R.id.tv_max_w)
            val tv_domain_num = dialog.findViewById<TextView>(R.id.tv_domain_num)
            val tv_prog_ratio = dialog.findViewById<TextView>(R.id.tv_prog_ratio)
            val tv_prog_percentage = dialog.findViewById<TextView>(R.id.tv_prog_percentage)
            val img_close = dialog.findViewById<ImageView>(R.id.img_close_w)
            val pb_progress_w = dialog.findViewById<ProgressBar>(R.id.pb_progress_w)
            ////////////////////////////////////////////

            val cor = correct_d
            pb_progress_w.setProgress(cor!!.toInt())
            pb_progress_w.max = totalQuiz_d!!.toInt()
            //Toast.makeText(context, "$smallestDomainModel   SP", Toast.LENGTH_LONG).show()
            tv_domain_num.text =
                "Domain ${domainId_d}: ${domain_name_d} "
            tv_max_w.text = incorrect_d
            tv_num_of_ques.text = incorrect_d
            tv_prog_ratio.text = "$cor / ${totalQuiz_d} Correct"
            tv_prog_percentage.text = "${progress_percentage_d} %"
            ////////////////////////////////////////////
            img_close.setOnClickListener(View.OnClickListener { dialog.dismiss() })
            tv_num_of_ques.text = seekBar.progress.toString()
            seekBar.max = incorrect_d!!.toInt()
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    // Update tv_num_of_ques with the new progress value
                    tv_num_of_ques.text = progress.toString()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })
            startButton.setOnClickListener {
                val quizSelected = seekBar.progress
                val bundle = Bundle()
                bundle.putString("QUIZ_CAT", "WEAKEST_SUBJECT")
                bundle.putInt("NO_Of_Quiz", quizSelected)
                bundle.putSerializable(
                    "WEAKEST_SUB_LIST",
                    ArrayList(weakestQuizListTesting.take(quizSelected))
                )
                Log.e(TAG, "Error getting quiz documents: $weakestQuizListTesting")

                navController!!.navigate(R.id.action_studyFragment_to_quizFragment, bundle)
                dialog.dismiss()
            }
        }

        if (dialogId == (R.layout.quit_quiz_dialog)) {
            val btnQuit: Button = dialog.findViewById(R.id.btn_quit)
            val btnSubmitQuiz: Button = dialog.findViewById(R.id.btn_submit_quiz)
            val btnContinue: Button = dialog.findViewById(R.id.btn_continue)

            btnQuit.setOnClickListener {
                // Dismiss the dialog
                dialog.dismiss()
                // Close the current fragment and go back to the previous one
                activity?.supportFragmentManager?.popBackStack()
            }
            btnSubmitQuiz.setOnClickListener {
                // Show a toast when the button is clicked
                Toast.makeText(requireContext(), "Quiz Submitted", Toast.LENGTH_SHORT).show()
            }
            btnContinue.setOnClickListener {
                // Only dismiss the dialog
                dialog.dismiss()
            }
        }
        if (dialogId == (R.layout.missed_questions_quiz_dialog)) {
            val startButton = dialog.findViewById<Button>(R.id.btn_start_mq)
            val seekBar = dialog.findViewById<SeekBar>(R.id.seekBar_mq)
            val tv_num_of_ques = dialog.findViewById<TextView>(R.id.tv_num_of_ques_m)
            val tv_max = dialog.findViewById<TextView>(R.id.tv_max)
            val tv_no_of_wrng_q = dialog.findViewById<TextView>(R.id.tv_no_of_wrng_q)
            val img_close = dialog.findViewById<ImageView>(R.id.img_close_m)
            img_close.setOnClickListener(View.OnClickListener { dialog.dismiss() })

            tv_no_of_wrng_q.text = missedQuizList.size.toString()
            tv_max.text = missedQuizList.size.toString()
            seekBar.max = missedQuizList.size
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    // Update tv_num_of_ques with the new progress value
                    tv_num_of_ques.text = progress.toString()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })

            startButton.setOnClickListener {
                val no_of_quiz = seekBar.progress
                val bundle = Bundle()
                bundle.putString("QUIZ_CAT", "MISSED_QUIZ")

                bundle.putSerializable(
                    "MISSED_QUIZ_LIST",
                    ArrayList(missedQuizList.take(no_of_quiz))
                )

                navController!!.navigate(R.id.action_studyFragment_to_quizFragment, bundle)

                dialog.dismiss()
            }
        }
        dialog.show()
    }

    fun getRandomQuestions() {
        val collectionPath = "/Exams/computer_science/Questions"
        val collectionRef = firestore.collection(collectionPath)

        collectionRef.get().addOnSuccessListener { querySnapshot ->
            val allQuestions = querySnapshot.toObjects(QuizModel::class.java)

            val shuffledQuestions = allQuestions.shuffled()
            val randomQuestions = shuffledQuestions.take(10)

            quick_10_quizList = randomQuestions as MutableList<QuizModel>

        }.addOnFailureListener { exception ->
            Log.e(TAG, "Error getting quiz documents: $exception")
        }
    }

    fun getRandomQuestionsTesting() {
        val collectionPath = "/Exams/computer_science/Questions"
        val collectionPath_2 = "/users/$user_id/history"
        val collectionRef = firestore.collection(collectionPath)
        val collectionRef_2 = firestore.collection(collectionPath_2)

        // Step 1: Fetch the existing documents' IDs in collectionPath_2
        collectionRef_2.get().addOnSuccessListener { querySnapshot_2 ->
            val existingQuestionIds = querySnapshot_2.documents.map { it.id }

            // Step 2: Filter out the documents from allQuestions that already exist in collectionPath_2
            collectionRef.get().addOnSuccessListener { querySnapshot ->
                val allQuestions = querySnapshot.toObjects(QuizModel::class.java)

                val newQuestions = allQuestions.filter { it.questionId !in existingQuestionIds }

                // Step 3: Save the remaining documents to quick_10_quizList
                if (newQuestions.size >= 10) {
                    quick_10_quizList =
                        newQuestions.toMutableList().take(10) as MutableList<QuizModel>
                } else {
                    quick_10_quizList = newQuestions.toMutableList()
                }
            }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting quiz documents: $exception")
                }
        }.addOnFailureListener { exception ->
            Log.e(TAG, "Error getting existing documents in collectionPath_2: $exception")
        }
    }

    companion object {
        fun newInstance(param1: String?, param2: String?): StudyFragment {
            val fragment = StudyFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }


    private fun setupCalendarView() {
        calendarAdapter.setDates(setup_calDates())
        calendarAdapter.setHighlightDate(docsList)
        calendarAdapter.notifyDataSetChanged()
    }

    private fun setup_QotD_CalendarView() {
        calendar_QotD_Adapter.setDates(setup_calDates())
        calendar_QotD_Adapter.setHighlightDate(docs_QotD_List)
        calendar_QotD_Adapter.notifyDataSetChanged()
    }

    private fun setup_calDates(): MutableList<Date> {

        val calendar: Calendar = Calendar.getInstance()
        val currentDate = calendar.time
        val endDate = currentDate.time // End date is the current date
        calendar.add(Calendar.DAY_OF_MONTH, -13) // Subtract 13 days from the current date
        val startDate = calendar.time // Start date is 13 days before the current date
        val dateList: MutableList<Date> = mutableListOf()
        var currentDate_long = startDate.time
        repeat(14) {
            dateList.add(Date(currentDate_long))
            currentDate_long += 86400000 // Add one day in milliseconds
        }
        return dateList
    }

    fun add_quiz() {
        val firestore = FirebaseFirestore.getInstance()

        val quizModel = QuizModel(
            questionId = "",
            answer = "Option D",
            question = "Your question",
            reason = "Your reason, THIS IS Option is the right answer",
            option_a = "Option A",
            option_b = "Option B",
            option_c = "Option C",
            option_d = "Option D",
            domain = "4",
            domain_name = "Domain D 4",
            subject = "Computer Science",

            /*       date = "22-07-2023",
                   attempted = "true",
                   correct = "false",
                   flagged = "",
                   quizCat = "QotD",
                   select_opt = "Option B"*/

        )

        val collectionPath = "/Exams/computer_science/Questions"
        //  val collectionPath = "/users/$user_id/history_of_QotD"

        val collectionRef = firestore.collection(collectionPath)
        collectionRef.add(quizModel)
            .addOnSuccessListener { documentReference ->
                val quizId = documentReference.id
                quizModel.questionId = quizId
                // Update the document with the document ID field
                documentReference.set(quizModel).addOnSuccessListener {
                    Log.d(TAG, "Quiz document added with ID: $quizId")
                }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error updating quiz document: $e")
                    }
                // Quiz document successfully added with auto-generated ID
                Log.d(TAG, "Quiz document added with ID: $quizId")

                ////////////////////////////////////////////////////

            }
    }


    fun fetch_missed_QuizList(key: String, value: String, quizCat: String?) {
        var quizList: MutableList<QuizModel> = mutableListOf()
        val firestore = FirebaseFirestore.getInstance()
        val collectionRef: CollectionReference

        collectionRef = firestore.collection("/users/$user_id/history")

        collectionRef.whereEqualTo(key, value)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val quizModel = document.toObject(QuizModel::class.java)
                    quizList.add(quizModel!!)
                }
                when (quizCat) {

                    "MISSED_QUIZ" -> {
                        missedQuizList = quizList
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Handle the error
                Log.e(ContentValues.TAG, "Error fetching documents: $exception")
            }
    }


    fun fetch_QotD(currentDate: String) {
        var quizList: MutableList<QuizModel> = mutableListOf()
        val firestore = FirebaseFirestore.getInstance()
        val collectionRef: CollectionReference

        collectionRef = firestore.collection("/Exams/computer_science/Question_Of_The_Day")

        collectionRef.whereGreaterThanOrEqualTo("date", "$currentDate")
            .whereLessThanOrEqualTo("date", "$currentDate")
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    for (document in querySnapshot.documents) {
                        val quizModel = document.toObject(QuizModel::class.java)
                        quizList.add(quizModel!!)
                    }
                    qotd_quiz = quizList
                    val bundle = Bundle()
                    bundle.putString("QUIZ_CAT", "QotD")
                    bundle.putSerializable(
                        "QotD",
                        ArrayList(qotd_quiz)
                    )
                    navController!!.navigate(R.id.action_studyFragment_to_quizFragment, bundle)

                } else {
                    Toast.makeText(
                        context,
                        "No quiz found for today",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }
            .addOnFailureListener { exception ->
                // Handle the error
                Log.e(ContentValues.TAG, "Error fetching documents: $exception")
            }
    }

    fun dom_tst() {
        val collectionPath = "/Exams/computer_science/Domains"
        val collectionRef = firestore.collection(collectionPath)

        collectionRef.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    // Process each document here
                    val documentId = document.id
                    fetch_domains_list("domain", documentId.toString())

                    no_of_Domains++
                }

            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting documents: $exception")
            }
    }

    fun fetch_weakest_quizList(value: Int) {
        var quizList: MutableList<QuizModel> = mutableListOf()
        val firestore = FirebaseFirestore.getInstance()
        val collectionRef: CollectionReference

        collectionRef = firestore.collection("/users/$user_id/history")

        collectionRef.whereEqualTo("domain", value.toString()).whereEqualTo("correct", "false")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val quizModel = document.toObject(QuizModel::class.java)
                    quizList.add(quizModel!!)
                }
                if (quizList.isNotEmpty()) {
                    weakestQuizListTesting = quizList
                } else {
                    Toast.makeText(
                        context,
                        "Congrats\nNo Weak subject",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }
            .addOnFailureListener { exception ->
                // Handle the error
                Log.e(ContentValues.TAG, "Error fetching documents: $exception")
            }
    }

    fun get_totalQuizList(domains: Int) {

        for (i in 1 until domains) {
            //fetch_wekest_sub_quiz("domain", i.toString())
        }
    }

    fun fetch_domains_list(key: String, value: String) {

        var dom_name: String? = null
        var totalQuiz = 0
        var wrongQuiz = 0
        var correct = 0
        var all_quizList: MutableList<QuizModel> = mutableListOf()
        val firestore = FirebaseFirestore.getInstance()
        val collectionRef = firestore.collection("/users/$user_id/history")
        collectionRef.whereEqualTo(key, value)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val quizModel = document.toObject(QuizModel::class.java)
                    all_quizList.add(quizModel!!)
                }

                totalQuiz = all_quizList.size
                incor_quiz_weakest_sub_list =
                    all_quizList.filter { it.correct == "false" }.toMutableList()
                wrongQuiz = incor_quiz_weakest_sub_list.size
                //  getDomainNameAndSave(value)
                // fill prog domain model
                if (totalQuiz != 0) {
                    val perc: Int
                    correct = totalQuiz - wrongQuiz
                    if (incor_quiz_weakest_sub_list.size != 0) {
                        dom_name = incor_quiz_weakest_sub_list.get(0).domain_name
                    }
                    perc = ((correct.toDouble() / totalQuiz.toDouble()) * 100).toInt()
                    val domainModel = DomainModel(
                        domainId = value.toString(),
                        totalQuiz = totalQuiz.toString(),
                        incorrect = wrongQuiz.toString(),
                        correct = correct.toString(),
                        domain_name = dom_name,
                        progress_percentage = perc.toString()
                    )
                    domainsList.add(domainModel)
                }
            }
            .addOnFailureListener { exception ->
                // Handle the error
                Log.e(ContentValues.TAG, "Error fetching documents: $exception")
            }
    }

    fun is_QotD_attempted() {
        var quiz_QotD: String = ""

        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val user_id: String? = firebaseAuth.currentUser?.uid
        val firestore = FirebaseFirestore.getInstance()

        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val collectionPath = "/users/$user_id/history_of_QotD"

        firestore.collection(collectionPath)
            .whereGreaterThanOrEqualTo("date", "$currentDate, 00:00:00")
            .whereLessThanOrEqualTo("date", "$currentDate, 23:59:59")
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    quiz_QotD = "true"
                    ll_btn_qotd.isClickable = false
                    Toast.makeText(
                        context,
                        "You already have attempted\nthe today's Quiz",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    quiz_QotD = "false"
                    fetch_QotD(currentDate)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error checking quiz documents for today's date: $exception")
            }
    }

    fun refreshFragment() {
        dom_tst()
        getAll_dom()
        fetch_missed_QuizList("correct", "false", "MISSED_QUIZ")
        get_studied_dates()
        get_QotD_studied_dates()
        getRandomQuestionsTesting()
    }

    private fun smallest_Dom() {
        if (domainsList.isNotEmpty()) {
            smallestDomainModel = domainsList.minByOrNull {
                it.progress_percentage?.toDoubleOrNull() ?: Double.MAX_VALUE
            }
            domainId_d = smallestDomainModel?.domainId
            domain_name_d = smallestDomainModel?.domain_name
            correct_d = smallestDomainModel?.correct
            incorrect_d = smallestDomainModel?.incorrect
            totalQuiz_d = smallestDomainModel?.totalQuiz
            progress_percentage_d = smallestDomainModel?.progress_percentage

        }

    }

    private fun generateDateList(formattedStartDate: String, formattedEndDate: String) {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        val startDate: Date = dateFormat.parse(formattedStartDate)!!
        val endDate: Date = dateFormat.parse(formattedEndDate)!!

        val calendar = Calendar.getInstance()
        calendar.time = startDate

        while (!calendar.time.after(endDate)) {
            val date: String? = dateFormat.format(calendar.time)
            val calenderModel = CalenderModel(
                date = date
            )
            dateList.add(calenderModel)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
    }

    fun get_studied_dates() {
        val path = "/users/$user_id/days_studied"

        val firestore = FirebaseFirestore.getInstance()
        val collectionRef = firestore.collection(path)
        listenerRegistration = collectionRef.addSnapshotListener { documentSnapshot, e ->
            if (e != null) {
                // Handle the error
                Log.e("Firestore", "Error fetching documents: $e")
                return@addSnapshotListener
            }
            docsList.clear()

            for (document in documentSnapshot?.documents ?: emptyList()) {
                val documentId = document.id
                docsList.add(documentId)
            }
            // calendarAdapter.notifyDataSetChanged()
            setupCalendarView()
        }
    }

    fun get_QotD_studied_dates() {
        val path = "/users/$user_id/history_of_QotD"

        val firestore = FirebaseFirestore.getInstance()
        val collectionRef = firestore.collection(path)
        listenerRegistration_QotD_cal = collectionRef.addSnapshotListener { querySnapshot, e ->
            if (e != null) {
                // Handle the error
                Log.e("Firestore", "Error fetching documents: $e")
                return@addSnapshotListener
            }
            docs_QotD_List.clear()
            for (document in querySnapshot?.documents ?: emptyList()) {
                val date = document.getString("date")
                if (date != null) {
                    docs_QotD_List.add(date)
                }
            }
            // Notify the adapter or update UI here if needed
            // calendarAdapter.notifyDataSetChanged()
            setup_QotD_CalendarView()
            find_streak_all_studied(docs_QotD_List)
//            no_of_streaks = findLastConsecutiveDates(docs_QotD_List)

        }
    }

    override fun onStart() {
        super.onStart()
        startFirestoreListener()
    }

    override fun onStop() {
        super.onStop()
        stopFirestoreListener()
    }

    private fun startFirestoreListener() {
        refreshFragment()
    }

    private fun stopFirestoreListener() {
        listenerRegistration?.remove()
        listenerRegistration_dom?.remove()
        listenerRegistration_QotD_cal?.remove()
        listenerRegistration = null
        listenerRegistration_dom = null
        listenerRegistration_QotD_cal = null
    }

    override fun onResume() {
        super.onResume()
        domainsList.clear()
        refreshFragment()

    }

    private fun setGreetingMessage() : String {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        val greetingMessage = when (currentHour) {
            in 0..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            else -> "Good Evening"
        }

        return greetingMessage
    }
    private fun getAll_dom() {
        val collectionRef = firestore.collection("/Exams/ComputerScience/Domains")

        // Add a snapshot listener to the collection reference
       listenerRegistration_dom = collectionRef.addSnapshotListener { querySnapshot, error ->
            if (error != null) {
                // Handle the error
                Log.e(TAG, "Error getting quizzes: $error")
                return@addSnapshotListener
            }

            // Clear the existing list of quizzes
            val dom_List: MutableList<DomainModel> = mutableListOf()

            // Process the documents in the snapshot
            for (document in querySnapshot!!.documents) {
                // Convert each document to a QuizModel object and add it to the list
                val domModel = document.toObject(DomainModel::class.java)

                domModel?.let {
                    dom_List.add(it)
                }

            }
           // Now you have all the quizzes in the quizList
            // You can do whatever you want with the list here, e.g., update UI, perform calculations, etc.
        }
    }


    private fun getGreetingMsg() {
        val docRef = firestore.document("/Exams/Greating_Msgs")

        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Document exists, retrieve the value of the "msg" field
                    val msgValue = documentSnapshot.getString("msg")
                    if (msgValue != null) {
                        // Use the value of the "msg" field
                        Log.d("Firestore", "Message: $msgValue")
                        tv_sub_greeting.text = msgValue
                    } else {
                        Log.d("Firestore", "Message field is null")
                    }
                } else {
                    Log.d("Firestore", "Document does not exist")
                }
            }
            .addOnFailureListener { exception ->
                // Handle the error while fetching the document
                Log.e("Firestore", "Error fetching document: ${docRef.path}", exception)
            }
    }

    fun find_streak_all_studied(datesList: List<String>) {
/*
        if (datesList.isEmpty()) {
            return 0
        }
*/

        val dateFormat = SimpleDateFormat("dd-MM-yyyy, HH:mm:ss", Locale.getDefault())
        val dateObjectsList = datesList.mapNotNull { dateString ->
            try {
                dateFormat.parse(dateString)
            } catch (e: Exception) {
                null
            }
        }.sorted()

        var maxConsecutiveDates = 1
        var currentConsecutiveDates = 1

        for (i in 1 until dateObjectsList.size) {
            val currentDate = dateObjectsList[i]
            val previousDate = dateObjectsList[i - 1]
            val diffInDays = ((currentDate.time - previousDate.time) / (1000 * 60 * 60 * 24)).toInt()

            if (diffInDays == 1) {
                currentConsecutiveDates++
                if (currentConsecutiveDates > maxConsecutiveDates) {
                    maxConsecutiveDates = currentConsecutiveDates
                }
            } else {
                currentConsecutiveDates = 1
            }
        }
        var msg: String
        var msg1: String
        var msg2: String
        var msg3: String

        if (maxConsecutiveDates > 1) {
            msg = maxConsecutiveDates.toString() + " days streak"
            msg2 = "You have ${maxConsecutiveDates} days study streak"
        } else{
            msg = maxConsecutiveDates.toString() + " day streak"
            msg2 = "You have ${maxConsecutiveDates} day study streak"
        }
        val calendar = Calendar.getInstance()
        val date_Format = SimpleDateFormat("MMMM dd", Locale.getDefault())
        val strDate = date_Format.format(calendar.time)
        tv_streak.text = msg

        if (dateList.size>1){
            msg1 = "You have Studied ${datesList.size} days since ${datesList.get(0).substring(0,11)}."
        }else{
            msg1 = "You have Studied ${datesList.size} day since ${datesList.get(0).substring(0,11)}."
        }
        tv_studied_updates.text = "Today is $strDate. $msg1 $msg2"

//        return maxConsecutiveDates
    }

}