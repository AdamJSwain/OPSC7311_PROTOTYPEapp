package com.example.opsc7311_prototypeapp.ui.view

import android.os.Bundle
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class ViewFragment : Fragment() {

    //All identities called
    private var _binding: FragmentViewBinding? = null
    private val binding get() = _binding!!
    val timeSheetEntries = mutableListOf<TimeSheetEntry>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewBinding.inflate(inflater, container, false)
        val endDate = binding.editTextEndDateE
        val startDate = binding.editTextStartDateE
        binding.buttonSelectDate.setOnClickListener()
        {
            //retrieveTimeSheetEntriesByUserIDAndDateRange(Worker.userInfo, startDate, endDate)
        }

        val root: View = binding.root
        return root
    }
    /*fun retrieveTimeSheetEntriesByUserIDAndDateRange(userID: String, startDate: Date, endDate: Date, listView: ListView) {
        val database = FirebaseDatabase.getInstance()
        val timeSheetEntriesRef = database.getReference("TimeSheetEntry")

        val query = timeSheetEntriesRef.orderByChild("User ID").equalTo(userID)

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
                        hours != null && price != null && totalPrice != null) {
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val date = dateFormat.parse(dateString)
                        if (date != null && date in startDate..endDate) {
                            val timeSheetEntry = TimeSheetEntry(category, dateString, description, hours, price, totalPrice, userID)
                            timeSheetEntries.add(timeSheetEntry)
                        }
                    }
                }

                val timeSheetAdapter = ArrayAdapter(listView.context, android.R.layout.simple_list_item_1, timeSheetEntries)
                listView.adapter = timeSheetAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error if retrieval is cancelled
            }
        })
    }
    */

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