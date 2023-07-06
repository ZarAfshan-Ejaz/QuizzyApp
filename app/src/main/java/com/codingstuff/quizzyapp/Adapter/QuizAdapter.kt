package com.codingstuff.quizzyapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codingstuff.quizzyapp.Model.QuizModel
import com.codingstuff.quizzyapp.R

class QuizAdapter(private val quizList: List<QuizModel>) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {
    private var isExplanationVisible = false

    inner class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Initialize views in the item layout
        val tv_question: TextView = itemView.findViewById(R.id.tv_question)

        val tv_op_a: TextView = itemView.findViewById(R.id.tv_op_a)
        val img_res_op_a: ImageView =itemView.findViewById(R.id.img_res_op_a)
        val tv_op_a_ans: TextView = itemView.findViewById(R.id.tv_correct_ans_a)

        val tv_op_b: TextView = itemView.findViewById(R.id.tv_op_b)
        val tv_op_b_ans: TextView = itemView.findViewById(R.id.tv_correct_ans_b)
        val img_res_op_b: ImageView =itemView.findViewById(R.id.img_res_op_b)

        val tv_op_c: TextView = itemView.findViewById(R.id.tv_op_c)
        val tv_op_c_ans: TextView = itemView.findViewById(R.id.tv_correct_ans_c)
        val img_res_op_c: ImageView =itemView.findViewById(R.id.img_res_op_c)

        val tv_op_d: TextView = itemView.findViewById(R.id.tv_op_d)
        val tv_op_d_ans: TextView = itemView.findViewById(R.id.tv_correct_ans_d)
        val img_res_op_d: ImageView =itemView.findViewById(R.id.img_res_op_d)

        val llOpDExpShow_a: LinearLayout = itemView.findViewById(R.id.ll_op_a_exp_show)
        val llOpDExpShow_b: LinearLayout = itemView.findViewById(R.id.ll_op_b_exp_show)
        val llOpDExpShow_c: LinearLayout = itemView.findViewById(R.id.ll_op_c_exp_show)
        val llOpDExpShow_d: LinearLayout = itemView.findViewById(R.id.ll_op_d_exp_show)

        val ll_main_op_a: LinearLayout = itemView.findViewById(R.id.ll_main_op_a)
        val ll_main_op_b: LinearLayout = itemView.findViewById(R.id.ll_main_op_b)
        val ll_main_op_c: LinearLayout = itemView.findViewById(R.id.ll_main_op_c)
        val ll_main_op_d: LinearLayout = itemView.findViewById(R.id.ll_main_op_d)

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


        val corr_opt = quiz.answer
        val reason = quiz.reason
        val ans = "$corr_opt\n+$reason"

        holder.ll_main_op_a.setOnClickListener(View.OnClickListener {
            if (corr_opt=="option_a"){
                holder.img_res_op_a.setImageResource(R.drawable.right_mark)
                holder.llOpDExpShow_a.visibility = View.VISIBLE
                holder.tv_op_a_ans.text = "$corr_opt\n$reason"
                holder.tv_op_a_ans.visibility = View.VISIBLE

            }else{
                holder.img_res_op_a.visibility = View.VISIBLE
                holder.img_res_op_a.setImageResource(R.drawable.close)
            }
            freezTheResult(holder)

        })
        holder.ll_main_op_b.setOnClickListener(View.OnClickListener {
            if (corr_opt =="option_b"){
                holder.llOpDExpShow_b.visibility = View.VISIBLE
                holder.img_res_op_b.setImageResource(R.drawable.right_mark)
                holder.tv_op_b_ans.text = "$corr_opt\n$reason"
                holder.tv_op_b_ans.visibility = View.VISIBLE


            }else{
                holder.img_res_op_b.visibility = View.VISIBLE
                holder.img_res_op_b.setImageResource(R.drawable.close)
            }
            freezTheResult(holder)

        })
        holder.ll_main_op_c.setOnClickListener(View.OnClickListener {
            if (corr_opt =="option_c"){
                holder.llOpDExpShow_c.visibility = View.VISIBLE
                holder.img_res_op_c.setImageResource(R.drawable.right_mark)
                holder.tv_op_c_ans.text = "$corr_opt\n$reason"
                holder.tv_op_c_ans.visibility = View.VISIBLE


            }else{
                holder.img_res_op_c.visibility = View.VISIBLE
                holder.img_res_op_c.setImageResource(R.drawable.close)
            }
            freezTheResult(holder)

        })
        holder.ll_main_op_d.setOnClickListener(View.OnClickListener {
            if (corr_opt =="option_d"){
                holder.img_res_op_d.visibility = View.VISIBLE
                holder.llOpDExpShow_d.visibility = View.VISIBLE
                holder.img_res_op_d.setImageResource(R.drawable.right_mark)
                holder.tv_op_d_ans.text = "$corr_opt\n$reason"

            }else{
                holder.img_res_op_d.visibility = View.VISIBLE
                holder.img_res_op_d.setImageResource(R.drawable.close)
            }
            freezTheResult(holder)
        })



        holder.llOpDExpShow_a.setOnClickListener(View.OnClickListener {
            toggleExplanation(holder.llOpDExpShow_a,holder.tv_op_a_ans)
        })
        holder.llOpDExpShow_b.setOnClickListener(View.OnClickListener {
            toggleExplanation(holder.llOpDExpShow_b,holder.tv_op_b_ans)
        })
        holder.llOpDExpShow_c.setOnClickListener(View.OnClickListener {
            toggleExplanation(holder.llOpDExpShow_c,holder.tv_op_c_ans)
        })
        holder.llOpDExpShow_d.setOnClickListener(View.OnClickListener {
            toggleExplanation(holder.llOpDExpShow_d,holder.tv_op_d_ans)
        })
    }

    override fun getItemCount(): Int {
        // Return the size of the dataset
        return quizList.size
    }

    private fun toggleExplanation(res_layout : LinearLayout, tv_corr_ans: TextView) {

        val textView: TextView = res_layout.getChildAt(0) as TextView
        val imageView: ImageView = res_layout.getChildAt(1) as ImageView

        if (isExplanationVisible) {
            // Hide explanation
           textView.text = "Show Explanation"
            imageView.setImageResource(R.drawable.drop_down_arrow)
            tv_corr_ans.visibility = View.GONE
        } else if (isExplanationVisible==false){
            // Show explanation
            textView.text = "Hide Explanation"
            imageView.setImageResource(R.drawable.close_menu_arrow_up)
            tv_corr_ans.visibility = View.VISIBLE

        }

        isExplanationVisible = !isExplanationVisible
    }
    fun freezTheResult(holder: QuizViewHolder){
        holder.ll_main_op_a.isClickable = false
        holder.ll_main_op_b.isClickable = false
        holder.ll_main_op_c.isClickable = false
        holder.ll_main_op_d.isClickable = false

    }
    fun showRightOptn(holder: QuizViewHolder, corr_opt:String, ans:String){

        if (corr_opt=="option_a"){
            holder.img_res_op_a.setImageResource(R.drawable.right_mark)
            holder.llOpDExpShow_a.visibility = View.VISIBLE
            holder.tv_op_a_ans.text = ans
            holder.tv_op_a_ans.visibility = View.VISIBLE

        }else{
            holder.img_res_op_a.visibility = View.VISIBLE
            holder.img_res_op_a.setImageResource(R.drawable.close)
        }
        freezTheResult(holder)
        freezTheResult(holder)
        if (corr_opt =="option_b"){
            holder.llOpDExpShow_b.visibility = View.VISIBLE
            holder.img_res_op_b.setImageResource(R.drawable.right_mark)
            holder.tv_op_b_ans.text = ans
            holder.tv_op_b_ans.visibility = View.VISIBLE


        }else{
            holder.img_res_op_b.visibility = View.VISIBLE
            holder.img_res_op_b.setImageResource(R.drawable.close)
        }
        freezTheResult(holder)
        if (corr_opt =="option_c"){
            holder.llOpDExpShow_c.visibility = View.VISIBLE
            holder.img_res_op_c.setImageResource(R.drawable.right_mark)
            holder.tv_op_c_ans.text = ans
            holder.tv_op_c_ans.visibility = View.VISIBLE


        }else{
            holder.img_res_op_c.visibility = View.VISIBLE
            holder.img_res_op_c.setImageResource(R.drawable.close)
        }
        freezTheResult(holder)
        if (corr_opt =="option_d"){
            holder.img_res_op_d.visibility = View.VISIBLE
            holder.llOpDExpShow_d.visibility = View.VISIBLE
            holder.img_res_op_d.setImageResource(R.drawable.right_mark)
            holder.tv_op_d_ans.text = ans

        }else{
            holder.img_res_op_d.visibility = View.VISIBLE
            holder.img_res_op_d.setImageResource(R.drawable.close)
        }
        freezTheResult(holder)

    }
    fun showWrongOptn(holder: QuizViewHolder, corr_opt:String?, ans:String){

        when (corr_opt){
            "option_a" ->{
                holder.img_res_op_a.setImageResource(R.drawable.right_mark)
                holder.llOpDExpShow_a.visibility = View.VISIBLE
                holder.llOpDExpShow_b.visibility = View.GONE
                holder.llOpDExpShow_c.visibility = View.GONE
                holder.llOpDExpShow_d.visibility = View.GONE

            }
            "option_b" ->{
                holder.llOpDExpShow_b.visibility = View.VISIBLE
                holder.img_res_op_b.setImageResource(R.drawable.right_mark)
                holder.llOpDExpShow_a.visibility = View.GONE
                holder.llOpDExpShow_c.visibility = View.GONE
                holder.llOpDExpShow_d.visibility = View.GONE
            }
            "option_c" ->{
                holder.llOpDExpShow_c.visibility = View.VISIBLE
                holder.img_res_op_c.setImageResource(R.drawable.right_mark)
                holder.llOpDExpShow_a.visibility = View.GONE
                holder.llOpDExpShow_b.visibility = View.GONE
                holder.llOpDExpShow_d.visibility = View.GONE

            }
            "option_d" ->{
                holder.img_res_op_d.visibility = View.VISIBLE
                holder.llOpDExpShow_d.visibility = View.VISIBLE
                holder.llOpDExpShow_a.visibility = View.GONE
                holder.llOpDExpShow_b.visibility = View.GONE
                holder.llOpDExpShow_c.visibility = View.GONE

            }
        }

        freezTheResult(holder)

    }
    fun showRightOptn(holder: QuizViewHolder, corr_opt:String?, ans:String){

        when (corr_opt){
            "option_a" ->{
                holder.img_res_op_a.setImageResource(R.drawable.right_mark)
                holder.img_res_op_a.visibility = View.VISIBLE
                holder.llOpDExpShow_a.visibility = View.VISIBLE
                holder.llOpDExpShow_b.visibility = View.GONE
                holder.llOpDExpShow_c.visibility = View.GONE
                holder.llOpDExpShow_d.visibility = View.GONE

            }
            "option_b" ->{
                holder.llOpDExpShow_b.visibility = View.VISIBLE
                holder.img_res_op_b.visibility = View.VISIBLE
                holder.img_res_op_b.setImageResource(R.drawable.right_mark)
                holder.llOpDExpShow_a.visibility = View.GONE
                holder.llOpDExpShow_c.visibility = View.GONE
                holder.llOpDExpShow_d.visibility = View.GONE
            }
            "option_c" ->{
                holder.llOpDExpShow_c.visibility = View.VISIBLE
                holder.img_res_op_c.visibility = View.VISIBLE

                holder.img_res_op_c.setImageResource(R.drawable.right_mark)
                holder.llOpDExpShow_a.visibility = View.GONE
                holder.llOpDExpShow_b.visibility = View.GONE
                holder.llOpDExpShow_d.visibility = View.GONE

            }
            "option_d" ->{
                holder.img_res_op_d.visibility = View.VISIBLE
                holder.img_res_op_d.setImageResource(R.drawable.right_mark)
                holder.llOpDExpShow_d.visibility = View.VISIBLE
                holder.llOpDExpShow_a.visibility = View.GONE
                holder.llOpDExpShow_b.visibility = View.GONE
                holder.llOpDExpShow_c.visibility = View.GONE

            }
        }

        freezTheResult(holder)

    }

}
