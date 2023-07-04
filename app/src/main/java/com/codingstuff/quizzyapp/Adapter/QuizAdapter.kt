package com.codingstuff.quizzyapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codingstuff.quizzyapp.Model.QuizModel
import com.codingstuff.quizzyapp.R

class QuizAdapter(private val quizList: List<QuizModel>) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    inner class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Initialize views in the item layout
        val tv_question: TextView = itemView.findViewById(R.id.tv_question)

        val tv_op_a: TextView = itemView.findViewById(R.id.tv_op_a)
        val img_res_op_a: ImageView =itemView.findViewById(R.id.img_res_op_a)
        val tv_op_a_ans: TextView = itemView.findViewById(R.id.tv_correct_ans_a)

        val tv_op_b: TextView = itemView.findViewById(R.id.tv_op_b)
        val tv_op_ans: TextView = itemView.findViewById(R.id.tv_correct_ans_b)
        val img_res_op_b: ImageView =itemView.findViewById(R.id.img_res_op_b)

        val tv_op_c: TextView = itemView.findViewById(R.id.tv_op_c)
        val tv_op_c_ans: TextView = itemView.findViewById(R.id.tv_correct_ans_c)
        val img_res_op_c: ImageView =itemView.findViewById(R.id.img_res_op_c)

        val tv_op_d: TextView = itemView.findViewById(R.id.tv_op_d)
        val tv_op_d_ans: TextView = itemView.findViewById(R.id.tv_correct_ans_d)
        val img_res_op_d: ImageView =itemView.findViewById(R.id.img_res_op_d)

       // val answerTextView: TextView = itemView.findViewById(R.id.answerTextView)
        // ... Add more views as needed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        // Inflate the item layout and create a ViewHolder
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.quiz_item, parent, false)
        return QuizViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        // Bind data to the views in the ViewHolder
        val quiz = quizList[position]
        holder.tv_question.text = quiz.question
        //holder.answerTextView.text = quiz.answer

        holder.tv_op_a.text = quiz.option_a
        holder.tv_op_b.text = quiz.option_b
        holder.tv_op_c.text = quiz.option_c
        holder.tv_op_d.text = quiz.option_d

        // ... Bind more data to other views as needed
    }

    override fun getItemCount(): Int {
        // Return the size of the dataset
        return quizList.size
    }
}
