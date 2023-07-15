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
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codingstuff.quizzyapp.CalendarAdapter
import com.codingstuff.quizzyapp.Model.QuizModel
import com.codingstuff.quizzyapp.R
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class StudyFragment : Fragment() {
    lateinit var tv_studied_updates: TextView
    lateinit var img_studied: ImageView
    lateinit var btn_tv_hide: ImageView
    lateinit var btn_tv_show: ImageView
    lateinit var rv_calendarView: RecyclerView
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
         rv_calendarView = view.findViewById<RecyclerView>(R.id.calendar_view_rv)
         tv_studied_updates = view.findViewById<TextView>(R.id.tv_studied_updates)
        btn_tv_hide = view.findViewById<ImageView>(R.id.btn_tv_hide)
        btn_tv_show = view.findViewById<ImageView>(R.id.btn_tv_show)
        val add_quiz_to_db: TextView = view.findViewById<TextView>(R.id.tv_add_quiz)
        val btn_update_exiting_quiz: TextView = view.findViewById<TextView>(R.id.btn_update_exiting_quiz)


        add_quiz_to_db.setOnClickListener(View.OnClickListener {
            add_quiz()
        })
        btn_update_exiting_quiz.setOnClickListener(View.OnClickListener {
            //removeAnyField()
           // update_multiple_fields()
            //add_quiz()
            moveToNewPath()
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
        setupCalendarView(rv_calendarView)
        ll_btn_qotd.setOnClickListener {
            val bundle = Bundle()
            //bundle.putString("START_TIMER", "value")
            bundle.putString("QUIZ_CAT", "QotD")
            navController!!.navigate(R.id.action_studyFragment_to_quizFragment,bundle)

        }
        ll_btn_build_quiz.setOnClickListener { //openBuildYourOwnQuizDialog();
            navController!!.navigate(R.id.action_studyFragment_to_buildYourOwnQuestionDialog)
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
            val bundle = Bundle()
            bundle.putString("QUIZ_CAT", "QUICK_10_QUIZ")

            navController!!.navigate(R.id.action_studyFragment_to_quizFragment,bundle)
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

        if (dialogId == (R.layout.select_quiz_time_dialog)){
            val img_close = dialog.findViewById<ImageView>(R.id.img_close_t)
            val startButton = dialog.findViewById<Button>(R.id.btn_start)
            val seekBar = dialog.findViewById<SeekBar>(R.id.seekBar_t)
            val tv_num_of_ques_t = dialog.findViewById<TextView>(R.id.tv_num_of_ques_t)

            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
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
                bundle.putString("QUIZ_CAT", "timed_quiz")
                bundle.putLong("millisecondsSelected", millisecondsSelected)

                navController!!.navigate(R.id.action_studyFragment_to_quizFragment,bundle)

                dialog.dismiss()
            }
            img_close.setOnClickListener(View.OnClickListener { dialog.dismiss()     })
        }
        if (dialogId == (R.layout.weakest_subject_quiz_dialog)){
            val startButton = dialog.findViewById<Button>(R.id.btn_dismiss)
            val seekBar = dialog.findViewById<SeekBar>(R.id.seekBar_w)
            val tv_num_of_ques = dialog.findViewById<TextView>(R.id.tv_num_of_ques)
            val img_close = dialog.findViewById<ImageView>(R.id.img_close_w)
            img_close.setOnClickListener(View.OnClickListener { dialog.dismiss()     })

            // Set the initial value of tv_num_of_ques to the current seekBar progress
            tv_num_of_ques.text = seekBar.progress.toString()

            // Set a listener to track seekBar progress changes
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
                val minutesSelected = seekBar.progress

                val bundle = Bundle()
                bundle.putString("QUIZ_CAT", "weakest_sub")
                bundle.putInt("NO_Of_Quiz", minutesSelected)

                navController!!.navigate(R.id.action_studyFragment_to_quizFragment, bundle)

                dialog.dismiss()
            }
        }
        if (dialogId == (R.layout.missed_questions_quiz_dialog)){
            val startButton = dialog.findViewById<Button>(R.id.btn_start_mq)
            val seekBar = dialog.findViewById<SeekBar>(R.id.seekBar_mq)
            val tv_num_of_ques = dialog.findViewById<TextView>(R.id.tv_num_of_ques_m)
            val img_close = dialog.findViewById<ImageView>(R.id.img_close_m)
            img_close.setOnClickListener(View.OnClickListener { dialog.dismiss()     })

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
                val no_of_quiz = seekBar.progress
                val bundle = Bundle()
                bundle.putString("QUIZ_CAT", "missed_quiz")
                bundle.putInt("NO_Of_Quiz", no_of_quiz)

                navController!!.navigate(R.id.action_studyFragment_to_quizFragment,bundle)

                dialog.dismiss()
            }
        }
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

        val quizModel = QuizModel(
                questionId = "",
                answer = "Option B",
                question = "Your question",
                reason = "Your reason, Option B is the right answer",
                option_a = "Option A",
                option_b = "Option B",
                option_c = "Option C",
                option_d = "Option D",
                domain = "3",
                subject = "Computer Science"

        )

        val collectionPath = "/Exams/ComputerScience/Questions"

        val collectionRef = firestore.collection(collectionPath)
        collectionRef.add(quizModel)
                .addOnSuccessListener { documentReference ->
                    val quizId = documentReference.id
                    quizModel.questionId = quizId
                    // Update the document with the document ID field
                    documentReference.set(quizModel) .addOnSuccessListener {
                        Log.d(TAG, "Quiz document added with ID: $quizId")
                    }
                            .addOnFailureListener { e ->
                                Log.e(TAG, "Error updating quiz document: $e")
                            }
                    // Quiz document successfully added with auto-generated ID
                    Log.d(TAG, "Quiz document added with ID: $quizId")

                    ////////////////////////////////////////////////////

                    /*   // Get a reference to the source document
        val sourceDocRef = FirebaseFirestore.getInstance().document("/Exams/ComputerScience/Questions")

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


    //add or update single new field
    fun updateExistingDocuments(){
        val firestore = FirebaseFirestore.getInstance()
        val collectionPath = "/Exams/ComputerScience/Questions"
        val collectionRef = firestore.collection(collectionPath)

        collectionRef.get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot.documents) {
                val quizId = document.id
                // Update the document with the document ID field
               // document.reference.update("questionId", quizId)
                document.reference.update("quiz_check", "new")
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

    // add or update more thn one fields
    fun update_multiple_fields() {
        val firestore = FirebaseFirestore.getInstance()
      //  val collectionPath = "/Exams/ComputerScience/Questions"
        val collectionPath = "/users/G897SE6IwSfSgpqiISIGNCBmSrI3/history"
        val collectionRef = firestore.collection(collectionPath)

        collectionRef.get()
                .addOnSuccessListener { querySnapshot ->
                    val batch = firestore.batch()

                    for (document in querySnapshot.documents) {
                        val quizId = document.id

                        val data = mapOf<String, Any>(
                                "subject" to "Computer Science",
                                "domain" to "1",
                                "date" to "01-07-2023, 02:30:00" // "dd-mm-yyyy, hh:mm:ss"
                                // Add more fields as needed
                        )

                        val documentRef = collectionRef.document(quizId)
                        batch.update(documentRef, data)
                    }

                    batch.commit()
                            .addOnSuccessListener {
                                Log.d(TAG, "Documents updated successfully")
                            }
                            .addOnFailureListener { e ->
                                Log.e(TAG, "Error updating documents: $e")
                            }
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error getting documents: $exception")
                }
    }

    fun removeAnyField(){
        val firestore = FirebaseFirestore.getInstance()
        val collectionPath = "/Exams/ComputerScience/Questions"
        val collectionRef = firestore.collection(collectionPath)

        collectionRef.get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot.documents) {
                // Remove the specific field from the document
                document.reference.update("documentId", FieldValue.delete())
                        .addOnSuccessListener {
                            Log.d(TAG, "Field removed from document: ${document.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "Error removing field from document: ${document.id}, $e")
                        }
            }
        }.addOnFailureListener { exception ->
            Log.e(TAG, "Error getting documents: $exception")
        }
    }

    private fun moveToNewPath(){
        val firestore = FirebaseFirestore.getInstance()

        val collectionRef_A = firestore.collection("/Exams/ComputerScience/Questions")
        val collectionRef_B = firestore.collection("/Exams/ComputerScience/Questions")

        collectionRef_A.get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot.documents) {
                val documentData = document.toObject(QuizModel::class.java)
                val documentId = document.id

                // Update the domain_name field
                documentData?.domain_name = "This is Domain 1 of Computer Science"

                // Create a new document with the updated data in path_B
                collectionRef_B.document(documentId).set(documentData!!)
                        .addOnSuccessListener {
                            // Document successfully duplicated
                            Log.d(TAG, "Document duplicated: $documentId")
                        }
                        .addOnFailureListener { exception ->
                            // Handle the error
                            Log.e(TAG, "Error duplicating document: $documentId", exception)
                        }
            }
        }
                .addOnFailureListener { exception ->
                    // Handle the error
                    Log.e(TAG, "Error fetching documents from path_A: $exception")
                }

    }

}
