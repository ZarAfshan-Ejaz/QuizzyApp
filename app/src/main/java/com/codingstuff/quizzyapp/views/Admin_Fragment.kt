package com.codingstuff.quizzyapp.views

import android.content.ContentValues
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.codingstuff.quizzyapp.Model.DomainModel
import com.codingstuff.quizzyapp.Model.QuizModel
import com.codingstuff.quizzyapp.R
import com.google.firebase.firestore.FirebaseFirestore

class Admin_Fragment : Fragment() {
    var quiz_cat: String? = null
    val firestore = FirebaseFirestore.getInstance()
    private lateinit var etQuestion: EditText
    private lateinit var etRightAns: EditText
    private lateinit var etReason: EditText
    private lateinit var etOptA: EditText
    private lateinit var etOptB: EditText
    private lateinit var etOptC: EditText
    private lateinit var etOptD: EditText
    private lateinit var etDomId: EditText
    private lateinit var etDomName: EditText
    private lateinit var etSubject: EditText
    private lateinit var etDate: EditText
    private lateinit var radioBtn_QotD: RadioButton
    private lateinit var radioBtn_QuizBank: RadioButton
    private lateinit var btnAddQuiz: Button
    private lateinit var radioGroup: RadioGroup

    var questionText: String? = null
    var rightAnsText: String? = null
    var reasonText: String? = null
    var optAText: String? = null
    var optBText: String? = null
    var optCText: String? = null
    var optDText: String? = null
    var domIdText: String? = null
    var domNameText: String? = null
    var subjectText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_, container, false)

        etQuestion = view.findViewById(R.id.et_question)
        etRightAns = view.findViewById(R.id.et_right_ans)
        etReason = view.findViewById(R.id.et_reason)
        etOptA = view.findViewById(R.id.et_opt_a)
        etOptB = view.findViewById(R.id.et_opt_b)
        etOptC = view.findViewById(R.id.et_opt_c)
        etOptD = view.findViewById(R.id.et_opt_d)
        etDomId = view.findViewById(R.id.et_dom_id)
        etDomName = view.findViewById(R.id.et_dom_name)
        etSubject = view.findViewById(R.id.et_subject)
        //checkBox = view.findViewById(R.id.cb_QotD)

        radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
        etDate = view.findViewById(R.id.et_date)
        radioBtn_QotD = view.findViewById(R.id.radioBtnQotD)
        radioBtn_QuizBank = view.findViewById(R.id.radioBtnQuizBank)
        btnAddQuiz = view.findViewById(R.id.btn_add_quz)



        val maxLength = 10
        val inputFilter = InputFilter.LengthFilter(maxLength)
        etDate.filters = arrayOf(inputFilter)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->

            when (checkedId) {
                R.id.radioBtnQotD -> {
                    if (radioBtn_QotD.isChecked) {
                        etDate.visibility = View.VISIBLE
                        quiz_cat = "QotD"
                    }
                }

                R.id.radioBtnQuizBank -> {
                    if (radioBtn_QuizBank.isChecked) {
                        etDate.text = null
                        etDate.visibility = View.GONE
                        quiz_cat = "QB"
                    }
                }
            }
        }


        //Set click listener for the "Add Quiz" button
        btnAddQuiz.setOnClickListener {
            questionText = etQuestion.text.toString().trim()
            rightAnsText = etRightAns.text.toString().trim()
            reasonText = etReason.text.toString().trim()
            optAText = etOptA.text.toString().trim()
            optBText = etOptB.text.toString().trim()
            optCText = etOptC.text.toString().trim()
            optDText = etOptD.text.toString().trim()
            domIdText = etDomId.text.toString().trim()
            domNameText = etDomName.text.toString().trim()
            subjectText = etSubject.text.toString().trim()
            val sub_path = subjectText!!.replace(" ", "_").trim().lowercase()

            if (questionText!!.isEmpty() || rightAnsText!!.isEmpty() || reasonText!!.isEmpty() ||
                optAText!!.isEmpty() || optBText!!.isEmpty() || optCText!!.isEmpty() || optDText!!.isEmpty() ||
                domIdText!!.isEmpty() || domNameText!!.isEmpty() || subjectText!!.isEmpty()
            ) {
                Toast.makeText(requireContext(), "All fields are required.", Toast.LENGTH_SHORT)
                    .show()
            }else{
                if (quiz_cat != null) {
                    if (quiz_cat == "QotD"){
                        val str_date = etDate.text.toString().trim()
                        if (str_date.isEmpty()) {
                            Toast.makeText( requireContext(),"Please enter a date for Question of the day",Toast.LENGTH_SHORT).show()
                        }else{
                            if (!str_date.isEmpty()) {
                                // Set a maximum character limit for the EditText
                                if (!isDateValid(etDate.text.toString())) {
                                    Toast.makeText(requireContext(),"Please enter a valid date\n in 'dd-mm-yyyy' format ",Toast.LENGTH_SHORT).show()
                                } else {
                                    add_quiz("/Exams/$sub_path/Question_Of_The_Day")
                                }
                            }
                        }
                    }
                    if (quiz_cat == "QB"){
                        add_quiz("/Exams/$sub_path/Questions")
                    }

                } else {
                    Toast.makeText(requireContext(), "Please select quiz category.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return view
    }


    /*
                radioBtnQotD.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {

                    etDate.visibility = View.VISIBLE

                    val maxLength = 10
                    val inputFilter = InputFilter.LengthFilter(maxLength)
                    etDate.filters = arrayOf(inputFilter)

                    val str_date = etDate.text.toString().trim()
                    if ( str_date.isEmpty()){
                        Toast.makeText(requireContext(), "Please enter a date for Question of the day", Toast.LENGTH_SHORT).show()
                    }else {
                        // Set a maximum character limit for the EditText

                        if (isDateValid(etDate.text.toString())) {
                            collectionPath = "/Exams/$sub_path/Question_Of_The_Day"
//                            add_quiz(collectionPath)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Please enter a valid date\n in 'dd-mm-yyyy' format ",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }else{
                    collectionPath = "/Exams/$sub_path/Questions"
                    etDate.visibility = View.GONE
                    etDate.text
//                    add_quiz(collectionPath)
                }
            }

    * */
    private fun isDateValid(date: String): Boolean {
        val regex = """\d{2}-\d{2}-\d{4}""".toRegex()
        return date.matches(regex)
    }

    private fun add_quiz(path : String) {


        val quizModel = QuizModel(
            questionId = "",
            answer = rightAnsText,
            question = questionText,
            reason = reasonText,
            option_a = optAText,
            option_b = optBText,
            option_c = optCText,
            option_d = optDText,
            domain = domIdText,
            domain_name = domNameText,
            subject = subjectText,
            date = etDate.text.toString(),

            )


        if (path!!.isNotEmpty()) {
            val collectionRef = firestore.collection(path)
            collectionRef.add(quizModel)
                .addOnSuccessListener { documentReference ->
                    val quizId = documentReference.id
                    quizModel.questionId = quizId
                    documentReference.set(quizModel).addOnSuccessListener {
                        add_quiz()
                        Toast.makeText(
                            requireContext(),
                            "Quiz document added with ID: $quizId",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                        .addOnFailureListener { e ->
                            Log.e(ContentValues.TAG, "Error updating quiz document: $e")
                        }
                    Log.d(ContentValues.TAG, "Quiz added successfully")

                }
        }

    }
    fun add_quiz() {
        val firestore = FirebaseFirestore.getInstance()

        val dom = DomainModel(
            domainId = domIdText,
            domain_name = domNameText,
            // other attributes
        )

        val sub_path = subjectText!!.replace(" ", "_").trim().lowercase()

        val collectionPath = "/Exams/$sub_path/Domains"

        val documentRef = firestore.document("$collectionPath/$domIdText")

        documentRef.set(dom)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Quiz document added with ID: $domIdText")
                // Handle success, if needed
            }
            .addOnFailureListener { e ->
                Log.e(ContentValues.TAG, "Error adding quiz document: $e")
                // Handle failure, if needed
            }
    }

}