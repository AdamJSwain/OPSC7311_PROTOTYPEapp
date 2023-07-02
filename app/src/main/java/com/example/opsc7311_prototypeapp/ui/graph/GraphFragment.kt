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
    private var database = FirebaseDatabase.getInstance( "https://opsc7311-prototypeapp-default-rtdb.europe-west1.firebasedatabase.app")

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
        retrieveTotalHoursWorked { totalHoursWorked -> //here is an issue
            // Update the chart with the retrieved total hours worked
            entries.addAll(totalHoursWorked)

            println("Retrieved total hours worked: $totalHoursWorked") // Print retrieved total hours worked

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
            leftAxis.axisMinimum = 0f // Minimum value for the Y axis
            leftAxis.axisMaximum = entries.maxByOrNull { it.y }?.y ?: 0f + 2f // Maximum value for the Y axis

            val rightAxis: YAxis = barChart.axisRight
            rightAxis.isEnabled = false // Disable the right Y axis

            // Calculate the maximum value for the Y axis
            val maximumHours = entries.maxByOrNull { it.y }?.y ?: 0f
            leftAxis.axisMaximum = maximumHours + 2f // Add a buffer of 2 units to the maximum

            // Retrieve the user's minimum and maximum goals from Firebase
            var userGoalsRef = database.getReference("UserGoal")
            val userId = userGoalsRef.orderByChild("User ID").equalTo(userID)
            retrieveUserGoalsByUserID(userId) { minimumGoal, maximumGoal ->

                println("Retrieved user goals - Minimum: $minimumGoal, Maximum: $maximumGoal") // Print retrieved user goals

                // Add horizontal limit lines for minimum and maximum goals
                val minimumLimitLine = LimitLine(minimumGoal, "Minimum Goal")
                minimumLimitLine.lineWidth = 4f
                minimumLimitLine.lineColor = Color.RED
                minimumLimitLine.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
                leftAxis.addLimitLine(minimumLimitLine)

                val maximumLimitLine = LimitLine(maximumGoal, "Maximum Goal")
                maximumLimitLine.lineWidth = 4f
                maximumLimitLine.lineColor = Color.GREEN
                minimumLimitLine.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
                leftAxis.addLimitLine(maximumLimitLine)

                barChart.description.isEnabled = false // Disable chart description

                barChart.invalidate() // Refresh the chart
            }
        }

            return root
        }


        // Retrieve the user's minimum and maximum goals from Firebase
        private fun retrieveUserGoalsByUserID(userIDQuery: Query, callback: (Float, Float) -> Unit) {
            val userGoalsRef = database.getReference("UserGoal")

            userIDQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Iterate through the snapshot to get the user ID
                    val userId = snapshot.children.firstOrNull()?.key

                    println("Retrieved user ID: $userId") // Print retrieved user ID

                    if (userId != null) {
                        val userGoalsQuery = userGoalsRef.child(userId)

                        userGoalsQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val minimumGoal =
                                    snapshot.child("Min Goal").getValue(Float::class.java) ?: 0f
                                val maximumGoal =
                                    snapshot.child("Max Goal").getValue(Float::class.java) ?: 0f

                                callback(minimumGoal, maximumGoal)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle error
                            }
                        })
                    } else {
                        println("User ID not found") // Print user ID not found
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "It broken", Toast.LENGTH_SHORT)
                }
            })
        }

    // Retrieve the total hours worked from Firebase
    private fun retrieveTotalHoursWorked(callback: (List<BarEntry>) -> Unit) {
        val timeSheetEntryRef = database.getReference("TimeSheetEntry")
        val userID = Worker.userInfo

        val userIdQuery = timeSheetEntryRef.orderByChild("User ID").equalTo(userID)

        userIdQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val totalHoursWorked = mutableListOf<BarEntry>()
                var dayIndex = 1

                for (snapshot in dataSnapshot.children) {
                    val hoursWorked =
                        snapshot.child("Hours Worked").getValue(Float::class.java) ?: 0f
                    totalHoursWorked.add(BarEntry(dayIndex.toFloat(), hoursWorked))
                    dayIndex++
                }

                println("Retrieved total hours worked: $totalHoursWorked") // Print retrieved total hours worked

                callback(totalHoursWorked) //here is an issue
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(requireContext(), "It broken", Toast.LENGTH_SHORT)
            }
        })
    }

    inner class DayAxisValueFormatter(private val numOfDays: Int) : ValueFormatter() {
        private var previousDayIndex: Int = -1 // Initialize with an invalid index

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt() - 1 // Subtract 1 from the index

            return if (index >= 0 && index < numOfDays && index != previousDayIndex) {
                previousDayIndex = index // Update the previous day index
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


