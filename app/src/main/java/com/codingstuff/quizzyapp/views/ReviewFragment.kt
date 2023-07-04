package com.codingstuff.quizzyapp.views

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.codingstuff.quizzyapp.R

class ReviewFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_review, container, false)
        val filterr = view.findViewById<TextView>(R.id.tv_filter)
        filterr.setOnClickListener { filterAlert() }
        return view
    }

    fun filterAlert() {
        val dialog: Dialog
        val items = arrayOf("PHP", "JAVA", "JSON", "C#", "Objective-C", "Python", "Kotlin", "C++", "Objective-C_2", "Python_2", "Kotlin_2", "C++_2")
        val itemsSelected = ArrayList<Int>()
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select Subjects:")
        builder.setMultiChoiceItems(items, null) { dialog, selectedItemId, isSelected ->
            if (isSelected) {
                itemsSelected.add(selectedItemId)
            } else if (itemsSelected.contains(selectedItemId)) {
                itemsSelected.remove(Integer.valueOf(selectedItemId))
            }
        }
                .setPositiveButton("Apply Filter!", null) // Set null to override the default behavior
                .setNegativeButton("Cancel") { dialog, id ->
                    // Your logic when the Cancel button is clicked
                }
        dialog = builder.create()
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(0, 0, 16, 16) // Adjust margins as per your preference
            val selectedCountTextView = TextView(context)
            selectedCountTextView.layoutParams = layoutParams
            updateSelectedCount(selectedCountTextView, itemsSelected.size) // Set initial count
            //   positiveButton.addView(selectedCountTextView);
            positiveButton.setOnClickListener {
                val selectedItems = StringBuilder()
                for (i in itemsSelected.indices) {
                    val index = itemsSelected[i]
                    selectedItems.append(items[index])
                    if (i != itemsSelected.size - 1) {
                        selectedItems.append(", ")
                    }
                }
                Toast.makeText(context, "Selected Subjects: $selectedItems", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    private fun updateSelectedCount(textView: TextView, count: Int) {
        val countText = "($count)"
        textView.text = countText
    }

    companion object {
        fun newInstance(param1: String?, param2: String?): ReviewFragment {
            val fragment = ReviewFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}