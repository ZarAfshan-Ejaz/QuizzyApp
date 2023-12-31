package com.codingstuff.quizzyapp

import android.app.Dialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codingstuff.quizzyapp.Model.QuizModel
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

open class BaseActivity : AppCompatActivity() {
    var weakestQuizList : MutableList<QuizModel> = mutableListOf()
    var missedQuizList : MutableList<QuizModel> = mutableListOf()
    var attemptedTimeQuiz : MutableList<QuizModel> = mutableListOf()
    val firestore = FirebaseFirestore.getInstance()

    var ques_cs = 0
    fun openDialog(dialogId: Int, context: Context?) {
        val dialog = Dialog(context!!)
        dialog.setContentView(dialogId)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window!!.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = layoutParams
        dialog.show()
    }

    open fun no_of_SpecificQuiz(context: Context?, key: String, value: Boolean, quizCat : String) {
        var quizList : MutableList<QuizModel> = mutableListOf()
        val firestore = FirebaseFirestore.getInstance()
        val collectionRef = firestore.collection("/users/G897SE6IwSfSgpqiISIGNCBmSrI3/history")

        collectionRef.whereEqualTo(key, value)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        val quizModel = document.toObject(QuizModel::class.java)
                        quizList.add(quizModel!!)

                    }
                    when (quizCat ){
                        "weakestQuizList" ->{
                            weakestQuizList = quizList
                        }
                        "weakestQuizList" ->{
                            missedQuizList = quizList

                        }
                    }

                  //  ques_cs =  quizList.size
                    // Perform further operations with the quizList containing the documents
                    Toast.makeText(context,quizList.size.toString()+"BA", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener { exception ->
                    // Handle the error
                    Log.e(ContentValues.TAG, "Error fetching documents: $exception")
                }

    }
    // add or update more thn one fields
    fun update_multiple_fields() {
        val firestore = FirebaseFirestore.getInstance()
        //  val collectionPath = "/Exams/computer_science/Questions"
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
                        Log.d(ContentValues.TAG, "Documents updated successfully")
                    }
                    .addOnFailureListener { e ->
                        Log.e(ContentValues.TAG, "Error updating documents: $e")
                    }
            }
            .addOnFailureListener { exception ->
                Log.e(ContentValues.TAG, "Error getting documents: $exception")
            }
    }

    fun removeAnyField() {
        val firestore = FirebaseFirestore.getInstance()
        val collectionPath = "/Exams/computer_science/Questions"
        val collectionRef = firestore.collection(collectionPath)

        collectionRef.get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot.documents) {
                // Remove the specific field from the document
                document.reference.update("documentId", FieldValue.delete())
                    .addOnSuccessListener {
                        Log.d(ContentValues.TAG, "Field removed from document: ${document.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.e(ContentValues.TAG, "Error removing field from document: ${document.id}, $e")
                    }
            }
        }.addOnFailureListener { exception ->
            Log.e(ContentValues.TAG, "Error getting documents: $exception")
        }
    }

    private fun moveToNewPath() {
        val firestore = FirebaseFirestore.getInstance()

        val collectionRef_A = firestore.collection("/Exams/computer_science/Questions")
        val collectionRef_B = firestore.collection("/Exams/computer_science/Questions")

        collectionRef_A.get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot.documents) {
                val documentData = document.toObject(QuizModel::class.java)
                val documentId = document.id

                // Update the domain_name field
              //  documentData?.domain_name = "This is Domain 1 of Computer Science"

                // Create a new document with the updated data in path_B
                collectionRef_B.document(documentId).set(documentData!!)
                    .addOnSuccessListener {
                        // Document successfully duplicated
                        Log.d(ContentValues.TAG, "Document duplicated: $documentId")
                    }
                    .addOnFailureListener { exception ->
                        // Handle the error
                        Log.e(ContentValues.TAG, "Error duplicating document: $documentId", exception)
                    }
            }
        }
            .addOnFailureListener { exception ->
                // Handle the error
                Log.e(ContentValues.TAG, "Error fetching documents from path_A: $exception")
            }

    }
    //add or update single new field
    fun updateExistingDocuments() {
        val firestore = FirebaseFirestore.getInstance()
        val collectionPath = "/Exams/computer_science/Questions"
        val collectionRef = firestore.collection(collectionPath)

        collectionRef.get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot.documents) {
                val quizId = document.id
                // Update the document with the document ID field
                // document.reference.update("questionId", quizId)
                document.reference.update("quiz_check", "new")
                    .addOnSuccessListener {
                        Log.d(ContentValues.TAG, "Document updated with ID: $quizId")
                    }
                    .addOnFailureListener { e ->
                        Log.e(ContentValues.TAG, "Error updating document: $e")
                    }
            }
        }.addOnFailureListener { exception ->
            Log.e(ContentValues.TAG, "Error getting documents: $exception")
        }


    }
    fun updateSubmittedQuiz(key: String?) {

        var quizList: MutableList<QuizModel> = mutableListOf()
        val firestore = FirebaseFirestore.getInstance()

        for (quizModel in quizList) {
            val documentRef = firestore.collection("/users/user_id/history").document(quizModel.questionId!!)
            documentRef.update("quizCat", key)
                .addOnSuccessListener {
                    Log.d(ContentValues.TAG, "Document updated with ID: ${quizModel.questionId}")
                }
                .addOnFailureListener { e ->
                    Log.e(ContentValues.TAG, "Error updating document with ID: ${quizModel.questionId}: $e")
                }
        }
    }

    //get All Documents with their values From Collection
    private fun getAllQuizzesFromCollection(collectionRef: CollectionReference) {
        // Add a snapshot listener to the collection reference
        collectionRef.addSnapshotListener { querySnapshot, error ->
            if (error != null) {
                // Handle the error
                Log.e(TAG, "Error getting quizzes: $error")
                return@addSnapshotListener
            }

            // Clear the existing list of quizzes
            val quizList: MutableList<QuizModel> = mutableListOf()

            // Process the documents in the snapshot
            for (document in querySnapshot!!.documents) {
                // Convert each document to a QuizModel object and add it to the list
                val quizModel = document.toObject(QuizModel::class.java)
                quizModel?.let {
                    quizList.add(it)
                }
            }

            // Now you have all the quizzes in the quizList
            // You can do whatever you want with the list here, e.g., update UI, perform calculations, etc.
        }
    }
// remove the document of a specific field value
private fun removeDocumentsWithNullQuestion() {

    val collectionRef = firestore.collection("/Exams/computer_science/Questions")

    collectionRef.whereEqualTo("question", null)
        .get()
        .addOnSuccessListener { querySnapshot ->
            val batch = collectionRef.firestore.batch()
            for (document in querySnapshot.documents) {
                batch.delete(document.reference)
            }

            batch.commit()
                .addOnSuccessListener {
                    // All documents with null "question" field deleted successfully
                    Log.d("Firestore", "Documents with null question deleted")
                }
                .addOnFailureListener { exception ->
                    // Handle the error while deleting documents
                    Log.e("Firestore", "Error deleting documents with null question", exception)
                }
        }
        .addOnFailureListener { exception ->
            // Handle the error while fetching documents in the collection
            Log.e("Firestore", "Error fetching documents from collection", exception)
        }
}
    fun get_no_of_documents_in_collection() {
        val collectionPath = "/Exams/computer_science/Domains"
        val collectionRef = firestore.collection(collectionPath)

        collectionRef.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    // Process each document here
                    val documentId = document.id
                  //  fetch_domains_list("domain", documentId.toString())

                   // no_of_Domains++
                }

            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting documents: $exception")
            }
    }

}