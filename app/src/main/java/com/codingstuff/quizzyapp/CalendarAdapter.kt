package com.codingstuff.quizzyapp
import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codingstuff.quizzyapp.Model.CalenderModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
class CalendarAdapter(private val context: Context) : RecyclerView.Adapter<CalendarAdapter.DateViewHolder>() {
    private val dates: MutableList<Date> = mutableListOf()
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val user_id: String? = firebaseAuth.currentUser?.uid
    val docsList: MutableList<String> = mutableListOf()

    private val dayNames = arrayOf("S", "M", "T", "W", "T", "F", "S")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_date, parent, false)
        return DateViewHolder(view)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {


        val datee = dates[position]
        val dateFormat = SimpleDateFormat("dd", Locale.getDefault())
        val day = dateFormat.format(datee)

        // Set the day name
        if (position < 7) {
            holder.textDayName.text = dayNames[position]
            holder.textDayName.visibility = View.VISIBLE
        } else {
            holder.textDayName.visibility = View.GONE
        }
        // Check if the date is in the list of highlighted dates and apply the background accordingly
/*
        if (highlightedDates.contains(date)) {
            holder.textDate.setBackgroundResource(R.drawable.circle_background)
        } else {
            holder.textDate.setBackgroundResource(0)
        }

*/

        val str_date = formatDateToString(datee)
        if ( docsList.contains(str_date)) {
            holder.textDate.setBackgroundResource(R.drawable.circle_background)
        } else {
            holder.textDate.setBackgroundResource(0)
        }


        // Set the date
        holder.textDate.text = day

        // Check if the date is the current date and apply underline
        val calendar = Calendar.getInstance()
        val today = calendar.time

        // Reset time fields to compare only dates
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val calendarDate = calendar.time
        if (isSameDate(datee, calendarDate)) {
            holder.textDate.paintFlags = holder.textDate.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        } else {
            holder.textDate.paintFlags = holder.textDate.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
        }
    }

    private fun isSameDate(date1: Date, date2: Date): Boolean {
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val dateString1 = dateFormat.format(date1)
        val dateString2 = dateFormat.format(date2)
        return dateString1 == dateString2
    }

    override fun getItemCount(): Int {
        return dates.size
    }

    fun setDates(newDates: List<Date>) {
        dates.clear()
        dates.addAll(newDates)
        notifyDataSetChanged()
    }
    fun setHighlightDate(dateListt: MutableList<String>) {
        docsList.addAll(dateListt)
    }


    fun formatDateToString(date: Date): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }


    inner class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textDayName: TextView = itemView.findViewById(R.id.textDayName)
        val textDate: TextView = itemView.findViewById(R.id.textDate)
    }

}
