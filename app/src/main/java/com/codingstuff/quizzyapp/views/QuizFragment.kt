package com.codingstuff.quizzyapp.views

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codingstuff.quizzyapp.Adapter.QuizAdapter
import com.codingstuff.quizzyapp.Adapter.RecyclerViewAdapter
import com.codingstuff.quizzyapp.Model.QuizModel
import com.codingstuff.quizzyapp.R
import com.google.firebase.firestore.FirebaseFirestore

class QuizFragment : Fragment() {
    val quizList = mutableListOf<QuizModel>()
    private var recyclerView: RecyclerView? = null
    private var leftArrow: ImageButton? = null
    private var rightArrow: ImageButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_quiz2, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        leftArrow = view.findViewById(R.id.leftArrow)
        rightArrow = view.findViewById(R.id.rightArrow)
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
        getQuiz()
        return view
    }

    private fun scrollRecyclerView(direction: Int) {
        val layoutManager = recyclerView!!.layoutManager as LinearLayoutManager?
        if (layoutManager != null) {
            val currentPosition = layoutManager.findFirstVisibleItemPosition()
            val newPosition = currentPosition + direction
            recyclerView!!.smoothScrollToPosition(newPosition)
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
    //real time quizzes
    fun getQuiz(){
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
        }.addOnFailureListener { exception ->
            Log.e(TAG, "Error getting quiz documents: $exception")
        }
    }
}