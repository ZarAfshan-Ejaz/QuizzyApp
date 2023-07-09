package com.codingstuff.quizzyapp.Adapter

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codingstuff.quizzyapp.Model.QuizModel
import com.codingstuff.quizzyapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class QuizAdapter(private val quizList: List<QuizModel>) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {
    private var isExplanationVisible = false
    lateinit var  quiz : QuizModel
    inner class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Initialize views in the item layout
        val tv_question: TextView = itemView.findViewById(R.id.tv_question)
        val tv_question_no: TextView = itemView.findViewById(R.id.tv_question_no)

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
        var buttonClicked = false

        // Bind data to the views in the ViewHolder
         quiz = quizList[position]
        holder.tv_question.text = quiz.question
        //holder.answerTextView.text = quiz.answer

        holder.tv_op_a.text = quiz.option_a
        holder.tv_op_b.text = quiz.option_b
        holder.tv_op_c.text = quiz.option_c
        holder.tv_op_d.text = quiz.option_d

        val index = position+1
        holder.tv_question_no.text = "Question $index / ${quizList.size}"

        val corr_opt = quiz.answer
        val reason = quiz.reason
        val ans = "$corr_opt\n+$reason"

        holder.ll_main_op_a.setOnClickListener(View.OnClickListener {
            answerSelected(holder,holder.llOpDExpShow_a,holder.img_res_op_a, holder.tv_op_a_ans,corr_opt,holder.tv_op_a.text.toString(),reason,holder.img_res_op_a, false,true)
        })
        holder.ll_main_op_b.setOnClickListener(View.OnClickListener {
            answerSelected(holder,holder.llOpDExpShow_b,holder.img_res_op_b, holder.tv_op_b_ans,corr_opt,holder.tv_op_b.text.toString(),reason,holder.img_res_op_b, false,true)
        })
        holder.ll_main_op_c.setOnClickListener(View.OnClickListener {
            answerSelected(holder,holder.llOpDExpShow_c,holder.img_res_op_c, holder.tv_op_c_ans,corr_opt,holder.tv_op_c.text.toString(),reason,holder.img_res_op_c, false,true)
        })
        holder.ll_main_op_d.setOnClickListener(View.OnClickListener {
            answerSelected(holder,holder.llOpDExpShow_d,holder.img_res_op_d, holder.tv_op_d_ans,corr_opt,holder.tv_op_d.text.toString(),reason,holder.img_res_op_d, false,true)
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
        if (corr_opt =="option_b"){
            holder.llOpDExpShow_b.visibility = View.VISIBLE
            holder.img_res_op_b.setImageResource(R.drawable.right_mark)
            holder.tv_op_b_ans.text = ans
            holder.tv_op_b_ans.visibility = View.VISIBLE


        }else{
            holder.img_res_op_b.visibility = View.VISIBLE
            holder.img_res_op_b.setImageResource(R.drawable.close)
        }
        if (corr_opt =="option_c"){
            holder.llOpDExpShow_c.visibility = View.VISIBLE
            holder.img_res_op_c.setImageResource(R.drawable.right_mark)
            holder.tv_op_c_ans.text = ans
            holder.tv_op_c_ans.visibility = View.VISIBLE


        }else{
            holder.img_res_op_c.visibility = View.VISIBLE
            holder.img_res_op_c.setImageResource(R.drawable.close)
        }
        if (corr_opt =="option_d"){
            holder.img_res_op_d.visibility = View.VISIBLE
            holder.llOpDExpShow_d.visibility = View.VISIBLE
            holder.img_res_op_d.setImageResource(R.drawable.right_mark)
            holder.tv_op_d_ans.text = ans

        }else{
            holder.img_res_op_d.visibility = View.VISIBLE
            holder.img_res_op_d.setImageResource(R.drawable.close)
        }

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
/*
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
*/
  fun answerSelected(
          holder: QuizViewHolder,
          llOpDExpShow_c : LinearLayout,
          img_res : ImageView,
          tv_answer:TextView,
          answer:String?,
          selected_op : String?,
          reason : String?,
          img_wrng_right:ImageView,
          flagged: Boolean,
          attempted:Boolean

  ){
    val correct:Boolean

    if (selected_op == answer){
          llOpDExpShow_c.visibility = View.VISIBLE
          img_res.setImageResource(R.drawable.right_mark)
          tv_answer.text = reason
          tv_answer.visibility = View.VISIBLE
          correct = true

      }else{
          img_wrng_right.visibility = View.VISIBLE
          img_wrng_right.setImageResource(R.drawable.close)
        correct = false

    }
      freezTheResult(holder)
      submitQuiz(quiz,selected_op, flagged,correct,attempted)

  }
    fun submitQuiz(
            quizModel: QuizModel,
            selected_op: String?,
            flagged: Boolean?,
            correct: Boolean?,
            attempted: Boolean?
    ) {
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val user_id: String? = firebaseAuth.currentUser?.uid

        val firestore = FirebaseFirestore.getInstance()

        val questionId = quizModel.questionId
        val collectionPath = "/users/$user_id/history/$questionId"

        val quizModelData = QuizModel(
                questionId = questionId,
                answer = quizModel.answer,
                question = quizModel.question,
                reason = quizModel.reason,
                option_a = quizModel.option_a,
                option_b = quizModel.option_b,
                option_c = quizModel.option_c,
                option_d = quizModel.option_d,
                select_opt = selected_op,
                flagged = flagged,
                correct = correct,
                attempted = attempted
        )

        val documentRef = firestore.document(collectionPath)
        documentRef.set(quizModelData)
                .addOnSuccessListener {
                    Log.d(TAG, "Quiz document added with ID: $questionId")
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error adding quiz document: $e")
                }
    }

}
