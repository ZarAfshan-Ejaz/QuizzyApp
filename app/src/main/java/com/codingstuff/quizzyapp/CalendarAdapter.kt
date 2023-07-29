package com.codingstuff.quizzyapp
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
class CalendarAdapter(private val context: Context) : RecyclerView.Adapter<CalendarAdapter.DateViewHolder>() {
    private val dates: MutableList<Date> = mutableListOf()
    private val dayNames = arrayOf("S", "M", "T", "W", "T", "F", "S")
    private var currentDate: Date? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_date, parent, false)
        return DateViewHolder(view)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val date = dates[position]
        val dateFormat = SimpleDateFormat("dd", Locale.getDefault())
        val day = dateFormat.format(date)

        // Set the day name
        if (position < 7) {
            holder.textDayName.text = dayNames[position]
            holder.textDayName.visibility = View.VISIBLE
        } else {
            holder.textDayName.visibility = View.GONE
        }
        if (dates[position] == currentDate) {
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

        if (isSameDate(date, calendarDate)) {
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
    fun setCurrentDate(date: Date) {
        currentDate = date
    }

    inner class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textDayName: TextView = itemView.findViewById(R.id.textDayName)
        val textDate: TextView = itemView.findViewById(R.id.textDate)
    }
}
