package com.codingstuff.quizzyapp.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.codingstuff.quizzyapp.Model.DomainModel
import com.codingstuff.quizzyapp.Model.QuizListModel
import com.codingstuff.quizzyapp.Model.QuizModel
import com.codingstuff.quizzyapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DomainsAdapter() : RecyclerView.Adapter<DomainsAdapter.DomListViewHolder>() {
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val user_id: String? = firebaseAuth.currentUser?.uid
    val firestore = FirebaseFirestore.getInstance()

    //    var quizList_combination: MutableList<QuizModel>
    private var domList: List<DomainModel>? = null
    fun setDomListModels(domList: List<DomainModel>?) {
        this.domList = domList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DomListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.byoq_subject_item, parent, false)
        return DomListViewHolder(view)
    }

    override fun onBindViewHolder(holder: DomListViewHolder, position: Int) {
        val model = domList!![position]
        holder.tv_dom_name.text = "Domain ${model.domainId}: ${model.domain_name}"

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                model.domainId?.let { removeDocument(it) }

            } else {
                model.domainId?.let { addDocumentWithId(it) }
                // If the checkbox is unchecked, you can handle it here if needed
            }
        }

    }

    override fun getItemCount(): Int {
        return if (domList == null) {
            0
        } else {
            domList!!.size
        }
    }

    inner class DomListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_dom_name: TextView
        val checkBox: CheckBox

        init {
            tv_dom_name = itemView.findViewById(R.id.tv_dom_name)
            checkBox = itemView.findViewById(R.id.cb_dom_ques)
        }
    }

    interface OnItemClickedListner {
        fun onItemClick(position: Int)
    }

    private fun removeDocument(docId: String) {
        val docRef = firestore.collection("/users/$user_id/temp_dom_filter").document(docId)

        docRef.delete()
            .addOnSuccessListener {
                // Document deleted successfully
                Log.d("Firestore", "Document deleted: ${docRef.path}")
            }
            .addOnFailureListener { exception ->
                // Handle the error while deleting the document
                Log.e("Firestore", "Error deleting document: ${docRef.path}", exception)
            }
    }

    private fun addDocumentWithId(docId: String) {
        val docRef = firestore.collection("/users/$user_id/temp_dom_filter").document(docId)
        val emptyData = emptyMap<String, Any>()

        docRef.set(emptyData)
            .addOnSuccessListener {
                // Document added successfully
                Log.d("Firestore", "Document added: ${docRef.path}")
            }
            .addOnFailureListener { exception ->
                // Handle the error while adding the document
                Log.e("Firestore", "Error adding document: ${docRef.path}", exception)
            }
    }

}