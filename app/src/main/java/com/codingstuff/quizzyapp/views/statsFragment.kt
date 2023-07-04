package com.codingstuff.quizzyapp.views

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codingstuff.quizzyapp.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import me.bastanfar.semicirclearcprogressbar.SemiCircleArcProgressBar

class statsFragment : Fragment() {
    // variable for our bar data.
    var barData: BarData? = null

    // variable for our bar data set.
    var barDataSet: BarDataSet? = null

    // array list for storing entries.
    var barEntriesArrayList: ArrayList<BarEntry>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_stats, container, false)
        val progressBar = view.findViewById<SemiCircleArcProgressBar>(R.id.semiCirclProgBar)
        val barChart = view.findViewById<BarChart>(R.id.idBarChart)
        proBar(progressBar)
        barChartQuizScore(barChart)
        return view
    }

    fun barChartQuizScore(barChart: BarChart) {
        barEntries

        // creating a new bar data set.
        barDataSet = BarDataSet(barEntriesArrayList, "Geeks for Geeks")

        // creating a new bar data and
        // passing our bar data set.
        barData = BarData(barDataSet)

        // below line is to set data
        // to our bar chart.
        barChart.data = barData

        // adding color to our bar data set.
        barDataSet!!.setColors(*ColorTemplate.MATERIAL_COLORS)

        // setting text color.
        barDataSet!!.valueTextColor = Color.BLACK

        // setting text size
        barDataSet!!.valueTextSize = 16f
        barChart.description.isEnabled = false
    }
    // creating a new array list

    // adding new entry to our array list with bar
    // entry and passing x and y axis value to it.
    private val barEntries: Unit
        private get() {
            // creating a new array list
            barEntriesArrayList = ArrayList<BarEntry>()

            // adding new entry to our array list with bar
            // entry and passing x and y axis value to it.
            barEntriesArrayList!!.add(BarEntry(1f, 4f))
            barEntriesArrayList!!.add(BarEntry(2f, 6f))
            barEntriesArrayList!!.add(BarEntry(3f, 8f))
            barEntriesArrayList!!.add(BarEntry(4f, 2f))
            barEntriesArrayList!!.add(BarEntry(5f, 4f))
            barEntriesArrayList!!.add(BarEntry(6f, 1f))
        }

    fun proBar(progressBar: SemiCircleArcProgressBar) {
        progressBar.setPercent(40)
        progressBar.setPercentWithAnimation(60)
        progressBar.setProgressBarColor(-0xff01)
        progressBar.setProgressPlaceHolderColor(-0xff0001)
        progressBar.setProgressBarWidth(32)
        progressBar.setProgressPlaceHolderWidth(40)
    }
}