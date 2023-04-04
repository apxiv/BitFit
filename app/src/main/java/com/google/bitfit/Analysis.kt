package com.google.bitfit

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class Analysis : Fragment() {
    private lateinit var painfulDays: TextView
    private lateinit var pieChart: PieChart
    private var happyCount: Int = 0
    private var sadCount: Int = 0
    private var neutralCount: Int = 0
    private var allDays: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_analysis, container, false)
        pieChart = view.findViewById(R.id.pieChart)
        painfulDays = view.findViewById(R.id.painfulDays)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO) {
            val numberOfPainfulDays = (requireActivity().application as CycleApplication).db.cycleDao().getNumberOfPainfulDays()
            allDays = (requireActivity().application as CycleApplication).db.cycleDao().getSize()
            sadCount = (requireActivity().application as CycleApplication).db.cycleDao().getSad()
            happyCount = (requireActivity().application as CycleApplication).db.cycleDao().getHappy()
            neutralCount = (requireActivity().application as CycleApplication).db.cycleDao().getNeutral()

            withContext(Dispatchers.Main) {
                painfulDays.text = "$numberOfPainfulDays / $allDays"
                setupPieChart()
                loadPieChartData(sadCount, neutralCount, happyCount, allDays)
            }
        }

    }

    private fun setupPieChart() {
        pieChart.isDrawHoleEnabled = true
        pieChart.setUsePercentValues(true)
        pieChart.setEntryLabelTextSize(12f)
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.centerText = "Mood"
        pieChart.setCenterTextSize(24f)
        pieChart.description.isEnabled = false
        val l = pieChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.isEnabled = true
    }

    private fun loadPieChartData(sad: Int, neutral: Int, happy: Int, all: Int) {
        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(sad.toFloat() / all, "Sad"))
        entries.add(PieEntry(neutral.toFloat() / all, "Neutral"))
        entries.add(PieEntry(happy.toFloat() / all, "Happy"))
        val colors: ArrayList<Int> = ArrayList()
        for (color in ColorTemplate.MATERIAL_COLORS) {
            colors.add(color)
        }
        for (color in ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color)
        }
        val dataSet = PieDataSet(entries, "Mood")
        dataSet.colors = colors
        val data = PieData(dataSet)
        data.setDrawValues(true)
        data.setValueFormatter(PercentFormatter(pieChart))
        data.setValueTextSize(16f)
        data.setValueTextColor(Color.BLACK)
        pieChart.data = data
        pieChart.invalidate()
    }
}