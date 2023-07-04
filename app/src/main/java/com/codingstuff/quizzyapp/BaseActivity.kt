package com.codingstuff.quizzyapp

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class BaseActivity : AppCompatActivity() {
    fun openDialog(dialogId: Int, context: Context?) {
        val dialog = Dialog(context!!)
        dialog.setContentView(dialogId)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window!!.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = layoutParams
        dialog.show()
    }
}