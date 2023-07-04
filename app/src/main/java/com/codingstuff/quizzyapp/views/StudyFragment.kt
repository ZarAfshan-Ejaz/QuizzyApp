package com.codingstuff.quizzyapp.views

import android.app.Dialog
import android.content.ContentValues.TAG
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codingstuff.quizzyapp.CalendarAdapter
import com.codingstuff.quizzyapp.Model.QuestionModel
import com.codingstuff.quizzyapp.Model.QuizModel
import com.codingstuff.quizzyapp.R
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class StudyFragment : Fragment() {
    private var navController: NavController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_study, container, false)
        val ll_btn_qotd = view.findViewById<LinearLayout>(R.id.ll_btn_qotd)
        val ll_btn_build_quiz = view.findViewById<LinearLayout>(R.id.ll_build_your_own_quiz)
        val ll_btn_missed_quiz = view.findViewById<LinearLayout>(R.id.ll_btn_missed_quiz)
        val ll_btn_timed_quiz = view.findViewById<LinearLayout>(R.id.ll_btn_timed_quiz)
        val ll_btn_weakest_subject_quiz = view.findViewById<LinearLayout>(R.id.ll_btn_weakest_subject_quiz)
        val ll_btn_quick_10_quiz = view.findViewById<LinearLayout>(R.id.ll_btn_quick_10_quiz)
//        val calendarView: CalendarView = view.findViewById<CalendarView>(R.id.calendar_view)
        val calendarViewRV: RecyclerView = view.findViewById<RecyclerView>(R.id.calendar_view_rv)
        val add_quiz_to_db: TextView = view.findViewById<TextView>(R.id.tv_add_quiz)

        add_quiz_to_db.setOnClickListener(View.OnClickListener {
            add_quiz()
        })

        setupCalendarView(calendarViewRV)
        ll_btn_qotd.setOnClickListener {
            navController!!.navigate(R.id.action_studyFragment_to_quizFragment)
        }
        ll_btn_build_quiz.setOnClickListener { //openBuildYourOwnQuizDialog();
            openDialog(R.layout.build_your_own_questions_dialog)
        }
        ll_btn_missed_quiz.setOnClickListener { //openMissedQuizDialog();
            openDialog(R.layout.missed_questions_quiz_dialog)
        }
        ll_btn_timed_quiz.setOnClickListener { //openMissedQuizDialog();
            openDialog(R.layout.select_quiz_time_dialog)
        }
        ll_btn_weakest_subject_quiz.setOnClickListener { //openMissedQuizDialog();
            openDialog(R.layout.weakest_subject_quiz_dialog)
        }
        ll_btn_quick_10_quiz.setOnClickListener { //openMissedQuizDialog();
            navController!!.navigate(R.id.action_studyFragment_to_quizFragment)

            //openDialog(R.layout.quit_quiz_dialog)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    private fun openDialog(dialogId: Int) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogId)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window!!.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = layoutParams
        dialog.show()
    }

    companion object {
        fun newInstance(param1: String?, param2: String?): StudyFragment {
            val fragment = StudyFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
    private lateinit var calendarAdapter: CalendarAdapter

    private fun setupCalendarView( calendarView: RecyclerView ) {
        calendarView.layoutManager = GridLayoutManager(requireContext(), 7)

        calendarAdapter = CalendarAdapter(requireContext())
        calendarView.adapter = calendarAdapter

        // Get the current date
        val calendar: Calendar = Calendar.getInstance()

        // Set the calendar to the first day of the current month
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        // Find the day of the week for the first day of the month (e.g., Sunday = 1, Monday = 2, etc.)
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        // Calculate the number of days to subtract to align with the desired starting day (e.g., Sunday = 1, Monday = 2, etc.)
        val daysToSubtract = if (firstDayOfWeek > Calendar.SUNDAY) firstDayOfWeek - Calendar.SUNDAY else 7

        // Calculate the start and end dates for two weeks
        val startDate: Date = calendar.time
        calendar.add(Calendar.DAY_OF_YEAR, 13)
        val endDate: Date = calendar.time

        // Generate a list of dates for two weeks (up to 14 dates)
        val dateList: MutableList<Date> = mutableListOf()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        var currentDate = startDate.time - (daysToSubtract * 86400000) // Subtract the days to align with the starting day
        var count = 0
        while (count < 14 && currentDate <= endDate.time) {
            dateList.add(Date(currentDate))
            currentDate += 86400000 // Add one day in milliseconds
            count++
        }

        // Set the dates in the adapter
        calendarAdapter.setDates(dateList)
    }

    fun add_quiz(   ) {
        val firestore = FirebaseFirestore.getInstance()

        val collectionPath = "/Exams/CEReGwH8PMG8zXVQqP3B/ComputerScience/Ay8RWesFncvEBVmuIAx0/Questions"
        val quizModel = QuizModel(
                answer = "Your answer",
                question = "Your question",
                reason = "Your reason",
                option_a = "Option A",
                option_b = "Option B",
                option_c = "Option C",
                option_d = "Option D"
        )

        val collectionRef = firestore.collection(collectionPath)
        collectionRef.add(quizModel)
                .addOnSuccessListener { documentReference ->
                    val quizId = documentReference.id
                    // Quiz document successfully added with auto-generated ID
                    Log.d(TAG, "Quiz document added with ID: $quizId")

                    ////////////////////////////////////////////////////

                    /*   // Get a reference to the source document
        val sourceDocRef = FirebaseFirestore.getInstance().document("/Exams/CEReGwH8PMG8zXVQqP3B/ComputerScience/Ay8RWesFncvEBVmuIAx0/Questions")

// Read the data from the source document
        sourceDocRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val sourceData = snapshot.data!!

                // Create a new document with the same structure
                val newDocRef = FirebaseFirestore.getInstance().collection("subjects").document("newDocumentId")
                newDocRef.set(sourceData)
                        .addOnSuccessListener {
                            Log.d(TAG, "New document created successfully")
                        }
                        .addOnFailureListener { error ->
                            Log.e(TAG, "Error creating new document: $error")
                        }
            } else {
                Log.d(TAG, "Source document does not exist")
            }
        }.addOnFailureListener { error ->
            Log.e(TAG, "Error getting source document: $error")
        }
*/
                }
    }

}