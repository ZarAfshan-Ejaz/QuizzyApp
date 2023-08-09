package com.codingstuff.quizzyapp.Adapter

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codingstuff.quizzyapp.BaseActivity
import com.codingstuff.quizzyapp.Model.QuizModel
import com.codingstuff.quizzyapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class QuizAdapter(private val quizList: List<QuizModel>, quizCat:String?, context: Context) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {
    private var isExplanationVisible = false
    lateinit var quiz : QuizModel
     var pre_Poss : Int = 0
    val context =context
    val quizCat = quizCat


    inner class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Initialize views in the item layout

        val tv_question: TextView = itemView.findViewById(R.id.tv_question)
        val tv_question_no: TextView = itemView.findViewById(R.id.tv_question_no)

        val tv_op_a: TextView = itemView.findViewById(R.id.tv_op_a)
        val img_res_op_a: ImageView =itemView.findViewById(R.id.img_res_op_a)

        val tv_op_b: TextView = itemView.findViewById(R.id.tv_op_b)
        val img_res_op_b: ImageView =itemView.findViewById(R.id.img_res_op_b)

        val tv_op_c: TextView = itemView.findViewById(R.id.tv_op_c)
        val img_res_op_c: ImageView =itemView.findViewById(R.id.img_res_op_c)

        val tv_op_d: TextView = itemView.findViewById(R.id.tv_op_d)
        val img_res_op_d: ImageView =itemView.findViewById(R.id.img_res_op_d)

       // val tv_corr_ans: TextView =itemView.findViewById(R.id.tv_corr_ans)
       // val ll_corr_ans: LinearLayout = itemView.findViewById(R.id.ll_corr_ans)

        val ll_main_op_a: LinearLayout = itemView.findViewById(R.id.ll_main_op_a)
        val ll_main_op_b: LinearLayout = itemView.findViewById(R.id.ll_main_op_b)
        val ll_main_op_c: LinearLayout = itemView.findViewById(R.id.ll_main_op_c)
        val ll_main_op_d: LinearLayout = itemView.findViewById(R.id.ll_main_op_d)

        var tvHide_a: TextView = itemView.findViewById(R.id.tv_hide_a)
        var tvShow_a: TextView = itemView.findViewById(R.id.tv_show_a)
        var tvAnswer_a: TextView = itemView.findViewById(R.id.tv_answer_a)
        var ll_ans_a: LinearLayout = itemView.findViewById(R.id.ll_ans_a)

        var tvHide_b: TextView = itemView.findViewById(R.id.tv_hide_b)
        var tvShow_b: TextView = itemView.findViewById(R.id.tv_show_b)
        var tvAnswer_b: TextView = itemView.findViewById(R.id.tv_answer_b)
        var ll_ans_b: LinearLayout = itemView.findViewById(R.id.ll_ans_b)

        var tvHide_c: TextView = itemView.findViewById(R.id.tv_hide_c)
        var tvShow_c: TextView = itemView.findViewById(R.id.tv_show_c)
        var tvAnswer_c: TextView = itemView.findViewById(R.id.tv_answer_c)
        var ll_ans_c: LinearLayout = itemView.findViewById(R.id.ll_ans_c)

        var tvHide_d: TextView = itemView.findViewById(R.id.tv_hide_d)
        var tvShow_d: TextView = itemView.findViewById(R.id.tv_show_d)
        var tvAnswer_d: TextView = itemView.findViewById(R.id.tv_answer_d)
        var ll_ans_d: LinearLayout = itemView.findViewById(R.id.ll_ans_d)


        // val answerTextView: TextView = itemView.findViewById(R.id.answerTextView)
        // ... Add more views as needed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        // Inflate the item layout and create a ViewHolder
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.quiz_item, parent, false)
        return QuizViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {

        if (pre_Poss < position){

            holder.img_res_op_a.visibility = View.GONE
            holder.img_res_op_b.visibility = View.GONE
            holder.img_res_op_c.visibility = View.GONE
            holder.img_res_op_d.visibility = View.GONE
           // holder.ll_corr_ans.visibility = View.GONE

        }

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
            pre_Poss = position
            answerSelected(holder.ll_main_op_a,holder,holder.img_res_op_a,corr_opt,holder.tv_op_a.text.toString(),reason,holder.img_res_op_a, "","true")
        })
        holder.ll_main_op_b.setOnClickListener(View.OnClickListener {
            pre_Poss = position
            answerSelected(holder.ll_main_op_b,holder,holder.img_res_op_b,corr_opt,holder.tv_op_b.text.toString(),reason,holder.img_res_op_b, "","true")

        })
        holder.ll_main_op_c.setOnClickListener(View.OnClickListener {
            pre_Poss = position
            answerSelected(holder.ll_main_op_c,holder,holder.img_res_op_c,corr_opt,holder.tv_op_c.text.toString(),reason,holder.img_res_op_c, "","true")
        })
        holder.ll_main_op_d.setOnClickListener(View.OnClickListener {
            pre_Poss = position
            answerSelected(holder.ll_main_op_d,holder,holder.img_res_op_d,corr_opt,holder.tv_op_d.text.toString(),reason,holder.img_res_op_d, "","true")
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

        }else{
            holder.img_res_op_a.visibility = View.VISIBLE
            holder.img_res_op_a.setImageResource(R.drawable.close)
        }
        if (corr_opt =="option_b"){
            holder.img_res_op_b.setImageResource(R.drawable.right_mark)
        }else{
            holder.img_res_op_b.visibility = View.VISIBLE
            holder.img_res_op_b.setImageResource(R.drawable.close)
        }
        if (corr_opt =="option_c"){
            holder.img_res_op_c.setImageResource(R.drawable.right_mark)

        }else{
            holder.img_res_op_c.visibility = View.VISIBLE
            holder.img_res_op_c.setImageResource(R.drawable.close)
        }
        if (corr_opt =="option_d"){
            holder.img_res_op_d.visibility = View.VISIBLE
            holder.img_res_op_d.setImageResource(R.drawable.right_mark)

        }else{
            holder.img_res_op_d.visibility = View.VISIBLE
            holder.img_res_op_d.setImageResource(R.drawable.close)
        }

    }

  fun answerSelected(
      ll_option: LinearLayout,
          holder: QuizViewHolder,
          img_res : ImageView,
          answer:String?,
          selected_op : String?,
          reason : String?,
          img_wrng_right:ImageView,
          flagged: String,
          attempted:String

  ){
    val correct:String

   // holder.ll_corr_ans.visibility = View.VISIBLE
    //holder.tv_corr_ans.text = "Answer :\n"+"$answer\n$reason"

    if (selected_op == answer){
        img_res.visibility = View.VISIBLE
        img_res.setImageResource(R.drawable.right_mark)
        ll_option.setBackgroundResource(R.drawable.card_border_correct)

        correct = "true"

      }else{
          img_wrng_right.visibility = View.VISIBLE
          img_wrng_right.setImageResource(R.drawable.close)
          ll_option.setBackgroundResource(R.drawable.card_border_wrong)


        correct = "false"

    }
      if (holder.tv_op_a.text.toString() == answer) {
          showReason( holder.ll_main_op_a, holder.ll_ans_a, holder.tvHide_a,holder.tvShow_a,holder.tvAnswer_a,reason,answer )

      } else if (holder.tv_op_b.text.toString() == answer) {
          showReason( holder.ll_main_op_b, holder.ll_ans_b, holder.tvHide_b,holder.tvShow_b,holder.tvAnswer_b,reason,answer )

      } else if (holder.tv_op_c.text.toString() == answer) {
          showReason( holder.ll_main_op_c, holder.ll_ans_c, holder.tvHide_c,holder.tvShow_c,holder.tvAnswer_c,reason,answer )

      } else if (holder.tv_op_d.text.toString() == answer) {
          showReason( holder.ll_main_op_d, holder.ll_ans_d, holder.tvHide_d,holder.tvShow_d,holder.tvAnswer_d,reason,answer )
      }
      freezTheResult(holder)

      val quizModelData = QuizModel(
              questionId = quiz.questionId,
              answer = quiz.answer,
              question = quiz.question,
              reason = quiz.reason,
              option_a = quiz.option_a,
              option_b = quiz.option_b,
              option_c = quiz.option_c,
              option_d = quiz.option_d,
              domain = quiz.domain,
              domain_name = quiz.domain_name,
              subject = quiz.subject,
              select_opt = selected_op,
              flagged = flagged,
              correct = correct,
              attempted = attempted,
              date = getCurrentTime(),
              quizCat = quizCat
      )


      submitQuiz(quizModelData)

    }

    fun submitQuiz(
            quizModel: QuizModel,
    ) {
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val user_id: String? = firebaseAuth.currentUser?.uid
        val fireStore = FirebaseFirestore.getInstance()

        val questionId = quizModel.questionId
        var collectionPath = ""

/*
        if (quizModel.quizCat == "QotD"){
            collectionPath = "/users/$user_id/history_of_QotD/$questionId"
        }else{
            collectionPath = "/users/$user_id/history/$questionId"
        }
*/
        collectionPath = "/users/$user_id/temp_history/$questionId"

        val documentRef = fireStore.document(collectionPath)
        documentRef.set(quizModel)
                .addOnSuccessListener {
                    Log.d(TAG, "Quiz document added with ID: $questionId")
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error adding quiz document: $e")
                }
    }
    fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy, HH:mm:ss", Locale.getDefault())
        val currentTime = Date()
        return dateFormat.format(currentTime)
    }

    fun showReason(ll_main: LinearLayout, ll_show: LinearLayout, tv_hide : TextView, tv_show : TextView, tv_answer : TextView, answer: String?, reason: String?){

        ll_main.setBackgroundResource(R.drawable.card_border_correct)

        ll_show.visibility = View.VISIBLE
        tv_show.setOnClickListener {
            tv_hide.visibility = View.VISIBLE
            tv_show.visibility = View.GONE
            tv_answer.text = "Right Answer :\n$answer"+ "\n\nReason :\n$reason"
            tv_answer.visibility = View.VISIBLE

        }

        tv_hide.setOnClickListener {
            tv_hide.visibility = View.GONE
            tv_show.visibility = View.VISIBLE
            tv_answer.visibility = View.GONE
        }

    }
}


