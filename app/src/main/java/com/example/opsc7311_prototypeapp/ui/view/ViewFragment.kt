package com.example.opsc7311_prototypeapp.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.opsc7311_prototypeapp.TimeSheetEntry
import com.example.opsc7311_prototypeapp.Worker
import com.example.opsc7311_prototypeapp.databinding.FragmentViewBinding
import com.example.opsc7311_prototypeapp.ui.entries.EntriesFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class ViewFragment : Fragment() {
    private var _binding: FragmentViewBinding? = null
    private val binding get() = _binding!!
    val database = FirebaseDatabase.getInstance("https://opsc7311-prototypeapp-default-rtdb.europe-west1.firebasedatabase.app")
    val timeSheetEntriesRef = database.getReference("TimeSheetEntry")
    val catRef = database.getReference("Category")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewBinding.inflate(inflater, container, false)
        val timeSheetListView = binding.listViewEntries

        binding.buttonSelectDate.setOnClickListener {
            val userID = Worker.userInfo
            val startDate = formatDate(2023, 6, 1) // Specify your start date
            val endDate = formatDate(2023, 7, 30) // Specify your end date
            retrieveCategoriesByUserID(userID) { categoryNames ->

                for (categoryName in categoryNames) {
                    val categorySpinner = binding.listViewEntries
                    val categoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, categoryNames)
                    categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    categorySpinner.adapter = categoryAdapter
                }
            }
        }

        return binding.root
    }

    private fun formatDate(year: Int, month: Int, dayOfMonth: Int): java.sql.Date {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth, 0, 0, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val utilDate = calendar.time
        return java.sql.Date(utilDate.time)
    }

    fun retrieveTimeSheetEntriesByUserIDAndDateRange(
        userID: String,
        startDate: Date,
        endDate: Date,
        listView: ListView
    ) {
        val query = timeSheetEntriesRef.orderByChild("Category").equalTo("Paid work")

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val timeSheetEntries = mutableListOf<TimeSheetEntry>()

                for (snapshot in dataSnapshot.children) {
                    val category = snapshot.child("Category").value as? String
                    val dateString = snapshot.child("Date").value as? String
                    val description = snapshot.child("Description").value as? String
                    val hours = snapshot.child("Hours Worked").value as? Int
                    val price = snapshot.child("Price per Hours").value as? Double
                    val totalPrice = snapshot.child("Total price of Work").value as? Double

                    if (category != null && dateString != null && description != null &&
                        hours != null && price != null && totalPrice != null
                    ) {
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val date = dateFormat.parse(dateString)

                        if (date != null && date >= startDate && date <= endDate) {
                            val timeSheetEntry = TimeSheetEntry(
                                category,
                                dateString,
                                description,
                                hours,
                                price,
                                totalPrice,
                                userID
                            )
                            timeSheetEntries.add(timeSheetEntry)
                        }
                    }
                }

                Log.d("ViewFragment", "Number of time sheet entries: ${timeSheetEntries.size}")

                val timeSheetAdapter =
                    ArrayAdapter(listView.context, android.R.layout.simple_list_item_1, timeSheetEntries)
                listView.adapter = timeSheetAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("ViewFragment", "Error retrieving time sheet entries: ${databaseError.message}")
            }
        })
    }

    fun retrieveCategoriesByUserID(userID: String, callback: (List<String>) -> Unit) {


        val query = timeSheetEntriesRef.orderByChild("User ID").equalTo(userID)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val categoryNames = mutableListOf<String>()

                for (snapshot in dataSnapshot.children) {
                    val categoryName = snapshot.child("Hours Worked").value.toString() as? String
                    categoryName?.let {
                        categoryNames.add(categoryName)
                    }
                }
                callback(categoryNames)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(requireContext(), "It broken", Toast.LENGTH_SHORT)
            }
        })
    }

    data class TimeSheetEntry(
        val category: String,
        val date: String,
        val description: String,
        val hours: Int,
        val price: Double,
        val totalPrice: Double,
        val userID: String
    )
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}