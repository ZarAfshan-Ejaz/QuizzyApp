package com.codingstuff.quizzyapp.ui.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codingstuff.quizzyapp.R

class SwitchExamDialogFragment : DialogFragment() {
    private var recyclerView: RecyclerView? = null

    //private MyAdapter adapter;
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Create a new AlertDialog builder
        val builder = AlertDialog.Builder(requireActivity())

        // Inflate the dialog layout
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.switch_exam_dialog_fragment, null)

        // Configure the RecyclerView
        recyclerView = dialogView.findViewById(R.id.rv_subjects)
        recyclerView!!.setLayoutManager(LinearLayoutManager(requireContext()))
        /*adapter = new MyAdapter(); // Replace with your custom adapter
        recyclerView.setAdapter(adapter);
*/
        // Set the dialog view
        builder.setView(dialogView)

        // Optionally, add buttons or other dialog customization
        builder.setPositiveButton("OK") { dialog: DialogInterface?, which: Int -> }
        builder.setNegativeButton("Cancel") { dialog: DialogInterface?, which: Int -> }

        // Create and return the dialog
        return builder.create()
    }
}