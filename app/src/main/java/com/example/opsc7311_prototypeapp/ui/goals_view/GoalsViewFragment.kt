package com.example.opsc7311_prototypeapp.ui.goals_view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.opsc7311_prototypeapp.databinding.FragmentGoalsViewBinding
import com.example.opsc7311_prototypeapp.ui.home.HomeViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.graphics.Color

class GoalsViewFragment : Fragment() {

    //all identities called
    private var _binding: FragmentGoalsViewBinding? = null
    private val binding get() = _binding!!

    //database reference
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentGoalsViewBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val chart: LineChart = binding.lineChartMonth

            val entries = generateEntriesForLastMonth()
            val dataSet = LineDataSet(entries, "Hours Worked")
            dataSet.color = Color.BLUE
            dataSet.setCircleColor(Color.BLUE)
            dataSet.setDrawValues(false)

            val goalDataSets: ArrayList<ILineDataSet> = ArrayList()

            val minimumGoal = 8f // Set the minimum goal value
            val maximumGoal = 12f // Set the maximum goal value

            val minimumGoalEntries = ArrayList<Entry>()
            val maximumGoalEntries = ArrayList<Entry>()

            for (entry in entries) {
                minimumGoalEntries.add(Entry(entry.x, minimumGoal))
                maximumGoalEntries.add(Entry(entry.x, maximumGoal))
            }

            val minimumGoalDataSet = LineDataSet(minimumGoalEntries, "Minimum Goal")
            minimumGoalDataSet.color = Color.GREEN
            minimumGoalDataSet.setDrawValues(false)
            minimumGoalDataSet.setDrawCircles(false)

            val maximumGoalDataSet = LineDataSet(maximumGoalEntries, "Maximum Goal")
            maximumGoalDataSet.color = Color.RED
            maximumGoalDataSet.setDrawValues(false)
            maximumGoalDataSet.setDrawCircles(false)

            goalDataSets.add(minimumGoalDataSet)
            goalDataSets.add(maximumGoalDataSet)

            val lineData = LineData(dataSet)
            lineData.dataSets.addAll(goalDataSets)

            chart.data = lineData
            chart.description = Description().apply {
                text = "Hours Worked and Goals over the last month"
            }
            chart.invalidate()

            val limitLine = LimitLine(maximumGoal, "Goal Limit")
            limitLine.lineWidth = 2f
            limitLine.lineColor = Color.RED

            chart.axisLeft.addLimitLine(limitLine)

            // Manually set the Y-axis range
            chart.axisLeft.axisMinimum = Math.min(minimumGoal, chart.axisLeft.axisMinimum)
            chart.axisLeft.axisMaximum = Math.max(maximumGoal, chart.axisLeft.axisMaximum)

        }
    private fun generateEntriesForLastMonth(): List<Entry> {
        val calendar = Calendar.getInstance()
        val entries: MutableList<Entry> = mutableListOf()

        for (i in 1..31) {
            val hours = getRandomHours()
            entries.add(Entry(i.toFloat(), hours))
            calendar.add(Calendar.DAY_OF_MONTH, -1)
        }

        return entries
    }

    private fun getRandomHours(): Float {
        return (8..12).random().toFloat() // Generate random hours between 8 and 12
    }
}