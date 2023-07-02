package com.example.opsc7311_prototypeapp.ui.graph

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.opsc7311_prototypeapp.databinding.FragmentGraphBinding
import com.example.opsc7311_prototypeapp.ui.home.HomeViewModel
import android.graphics.Color
import com.example.opsc7311_prototypeapp.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.database.*
import android.widget.Toast
import com.example.opsc7311_prototypeapp.Worker

class GraphFragment : Fragment() {

    //all identities called
    private var _binding: FragmentGraphBinding? = null
    private val binding get() = _binding!!

    // Firebase references
    private var database = FirebaseDatabase.getInstance()

    //could create issues bec not in OnCreate() (?)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentGraphBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val userID = Worker.userInfo

        val barChart = root.findViewById<BarChart>(R.id.chartBar)

        val entries = ArrayList<BarEntry>()
        // Remove the hardcoded entries and retrieve the total hours worked from Firebase
        retrieveTotalHoursWorked { totalHoursWorked ->
            // Update the chart with the retrieved total hours worked
            entries.addAll(totalHoursWorked)

            val dataSet = BarDataSet(entries, "Total Hours Worked")
            dataSet.color = Color.BLUE

            val barData = BarData(dataSet)
            barChart.data = barData

            // Customize X axis labels
            val xAxis: XAxis = barChart.xAxis
            xAxis.valueFormatter = DayAxisValueFormatter(totalHoursWorked.size) // Custom formatter for day labels
            xAxis.position = XAxis.XAxisPosition.BOTTOM // Place labels at the bottom of the chart

            // Customise Y axis labels
            val leftAxis: YAxis = barChart.axisLeft
            leftAxis.setDrawLabels(true) // Show Y axis labels

            val rightAxis: YAxis = barChart.axisRight
            rightAxis.isEnabled = false // Disable the right Y axis

            // Calculate the maximum value for the Y axis
            val maximumHours = entries.maxByOrNull { it.y }?.y ?: 0f
            leftAxis.axisMaximum = maximumHours + 2f // Add a buffer of 2 units to the maximum

            // Retrieve the user's minimum and maximum goals from Firebase
            //val userId = "8Fyc9UJEfAOhwBS2aLoNHrFP0gp2" // Replace with the actual user ID
            var userGoalsRef = database.getReference("UserGoal")
            val userId = userGoalsRef.orderByChild("User ID").equalTo(userID)
            retrieveUserGoalsByUserID(userId.toString()) { minimumGoal, maximumGoal ->
                // Add horizontal limit lines for minimum and maximum goals
                val minimumLimitLine = LimitLine(minimumGoal, "Minimum Goal")
                minimumLimitLine.lineWidth = 2f
                minimumLimitLine.lineColor = Color.RED
                leftAxis.addLimitLine(minimumLimitLine)

                val maximumLimitLine = LimitLine(maximumGoal, "Maximum Goal")
                maximumLimitLine.lineWidth = 2f
                maximumLimitLine.lineColor = Color.GREEN
                leftAxis.addLimitLine(maximumLimitLine)

                barChart.description.isEnabled = false // Disable chart description

                barChart.invalidate() // Refresh the chart
            }
        }

            return root
        }


        // Retrieve the user's minimum and maximum goals from Firebase
        private fun retrieveUserGoalsByUserID(userID: String, callback: (Float, Float) -> Unit) {
            var userGoalsRef = database.getReference("UserGoal")
            val userGoalsQuery = userGoalsRef.child(userID)

            userGoalsQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val minimumGoal = snapshot.child("Min Goal").getValue(Float::class.java) ?: 0f
                    val maximumGoal = snapshot.child("Max Goal").getValue(Float::class.java) ?: 0f

                    callback(minimumGoal, maximumGoal)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }

    // Retrieve the total hours worked from Firebase
    private fun retrieveTotalHoursWorked(callback: (List<BarEntry>) -> Unit) {
        var timeSheetEntryRef = database.getReference("TimeSheetEntry")
        val userID = Worker.userInfo

        val userId = timeSheetEntryRef.orderByChild("User ID").equalTo(userID)

        val timeSheetEntriesQuery = timeSheetEntryRef.orderByChild("User ID").equalTo(userId.toString())

        timeSheetEntriesQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val totalHoursWorked = mutableListOf<BarEntry>()

                for (snapshot in dataSnapshot.children) {
                    val dayIndex = snapshot.child("Day Index").getValue(Int::class.java) ?: 0
                    val hoursWorked =
                        snapshot.child("Hours Worked").getValue(Float::class.java) ?: 0f

                    totalHoursWorked.add(BarEntry(dayIndex.toFloat(), hoursWorked))
                }

                callback(totalHoursWorked)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    inner class DayAxisValueFormatter(private val numOfDays: Int) : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            return if (index >= 0 && index < numOfDays) {
                "Day ${index + 1}"
            } else {
                ""
            }
        }
    }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }


