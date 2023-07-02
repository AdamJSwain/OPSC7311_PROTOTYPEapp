package com.example.opsc7311_prototypeapp.ui.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.opsc7311_prototypeapp.TimeSheetEntry
import com.example.opsc7311_prototypeapp.Worker
import com.example.opsc7311_prototypeapp.databinding.FragmentViewBinding
import com.example.opsc7311_prototypeapp.ui.entries.EntriesFragment
import com.google.firebase.database.*
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
        val textViewStart: TextView = binding.txtStartDate
        val txtEnd: TextView = binding.txtEndDate
        textViewStart.text = SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())
        txtEnd.text = SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())

        var cal = Calendar.getInstance()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "yyyy.MM.dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
                textViewStart.text = sdf.format(cal.time)

            }
        val dateSetListener1 =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "yyyy.MM.dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.getDefault())

                txtEnd.text = sdf.format((cal.time))
            }

        textViewStart.setOnClickListener {
            DatePickerDialog(
                requireContext(), dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        txtEnd.setOnClickListener {
            DatePickerDialog(
                requireContext(), dateSetListener1,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        binding.buttonSelectDate.setOnClickListener {
            val userID = Worker.userInfo
            val startDate = formatDate(2023, 6, 1) // Specify your start date
            val endDate = formatDate(2023, 7, 30) // Specify your end date
            retrieveCategoriesByUserID(userID) { entryNames ->

                for (Description in entryNames) {
                    val categorySpinner = binding.listViewEntries
                    val categoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, entryNames)
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


    fun retrieveCategoriesByUserID(userID: String, callback: (List<String>) -> Unit) {


        val query = timeSheetEntriesRef.orderByChild("User ID").equalTo(userID)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val categoryNames = mutableListOf<String>()

                for (snapshot in dataSnapshot.children) {
                    val categoryName = snapshot.child("Description").value.toString() as? String
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