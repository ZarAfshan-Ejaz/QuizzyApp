package com.codingstuff.quizzyapp.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.ProgressBar

class HalfCircleProgressBar : ProgressBar {
    private var rectF: RectF? = null
    private var paint: Paint? = null

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        rectF = RectF()
        paint = Paint()
        paint!!.isAntiAlias = true
        paint!!.style = Paint.Style.FILL
        paint!!.color = -0xff0100 // Set the desired color of the progress
    }

    @Synchronized
    override fun onDraw(canvas: Canvas) {
        val sweepAngle = progress * 180f / max
        rectF!![paddingLeft.toFloat(), paddingTop.toFloat(), (width - paddingRight).toFloat()] = (height - paddingBottom).toFloat()
        canvas.drawArc(rectF!!, 180f, sweepAngle, true, paint!!)
    }
}