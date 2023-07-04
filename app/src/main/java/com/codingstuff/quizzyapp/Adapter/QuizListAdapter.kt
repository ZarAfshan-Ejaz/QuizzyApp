package com.codingstuff.quizzyapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codingstuff.quizzyapp.Adapter.QuizListAdapter.QuizListViewHolder
import com.codingstuff.quizzyapp.Model.QuizListModel
import com.codingstuff.quizzyapp.R

class QuizListAdapter(private val onItemClickedListner: OnItemClickedListner) : RecyclerView.Adapter<QuizListViewHolder>() {
    private var quizListModels: List<QuizListModel>? = null
    fun setQuizListModels(quizListModels: List<QuizListModel>?) {
        this.quizListModels = quizListModels
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_quiz, parent, false)
        return QuizListViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizListViewHolder, position: Int) {
        val model = quizListModels!![position]
        holder.title.text = model.title
        Glide.with(holder.itemView).load(model.image).into(holder.quizImage)
    }

    override fun getItemCount(): Int {
        return if (quizListModels == null) {
            0
        } else {
            quizListModels!!.size
        }
    }

    inner class QuizListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val title: TextView
        val quizImage: ImageView
        private val constraintLayout: ConstraintLayout

        init {
            title = itemView.findViewById(R.id.quizTitleList)
            quizImage = itemView.findViewById(R.id.quizImageList)
            constraintLayout = itemView.findViewById(R.id.constraintLayout)
            constraintLayout.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            onItemClickedListner.onItemClick(adapterPosition)
        }
    }

    interface OnItemClickedListner {
        fun onItemClick(position: Int)
    }
}