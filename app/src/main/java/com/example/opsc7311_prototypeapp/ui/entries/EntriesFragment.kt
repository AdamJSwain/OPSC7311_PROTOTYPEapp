package com.example.opsc7311_prototypeapp.ui.entries

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.opsc7311_prototypeapp.Category
import com.example.opsc7311_prototypeapp.R
import com.example.opsc7311_prototypeapp.TimeSheetEntry
import com.example.opsc7311_prototypeapp.databinding.FragmentEntriesBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class EntriesFragment : Fragment() {

    private var _binding: FragmentEntriesBinding? = null
    private val binding get() = _binding!!

    //declaring all variables
    private lateinit var datePicker: DatePicker
    private lateinit var timePickerStartTime: TimePicker
    private lateinit var timePickerEndTime: TimePicker
    private lateinit var editTextDescription: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var editPrice: EditText
    private lateinit var buttonAddPhoto: Button
    private lateinit var buttonSaveEntry: Button
    private lateinit var dbRef2: DatabaseReference
    private lateinit var imageViewPhoto: ImageView
    private var selectedImage: Bitmap? = null
    private lateinit var buttonTakePhoto: Button

    companion object {
        private const val REQUEST_IMAGE_PICK = 1
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val DATE_FORMAT = "yyyy-MM-dd"
        private const val TIME_FORMAT = "HH:mm"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEntriesBinding.inflate(inflater, container, false)
        val view = binding.root

        //database reference
        dbRef2 = FirebaseDatabase.getInstance().reference.child("timesheet_entries")
        //calling the variables
        datePicker = binding.datePicker
        timePickerStartTime = binding.timePickerStartTime
        timePickerEndTime = binding.timePickerEndTime
        editTextDescription = binding.editTextDescription
        categorySpinner = binding.categorySpinner
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, ArrayList())
        categorySpinner.adapter = adapter
        editPrice = binding.editPrice
        buttonAddPhoto = binding.buttonAddPhoto
        buttonSaveEntry = binding.buttonSaveEntry
        buttonTakePhoto = binding.buttonTakePhoto

        buttonTakePhoto.setOnClickListener{
            dispatchTakePictureIntent()
        }

        // Handle add image button click
        buttonAddPhoto.setOnClickListener {
            openGallery()
        }

        // Handle save entries button
        buttonSaveEntry.setOnClickListener {
            saveTimesheetEntry()
        }

        return view

    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }
    //this will allow the image taken by the user to be displayed in the imageView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        imageViewPhoto = view.findViewById(R.id.imageViewPhoto)

    }

    fun updateData(data: ArrayList<String>) {
        adapter.clear()
        adapter.addAll(data)
        adapter.notifyDataSetChanged()
    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    private fun saveTimesheetEntry() {
        val date = formatDate(datePicker.year, datePicker.month, datePicker.dayOfMonth)
        val startTime = formatTime(timePickerStartTime.hour, timePickerStartTime.minute)
        val endTime = formatTime(timePickerEndTime.hour, timePickerEndTime.minute)
        val description = editTextDescription.text.toString()
        val category = categorySpinner.selectedItem.toString()
        val price = editPrice.text.toString().toDoubleOrNull() ?: 0.0

        val hoursWorked = calculateHoursWorked(startTime, endTime)
        val totalPrice = hoursWorked * price

        val entry = TimeSheetEntry(date, startTime, endTime, description, category, hoursWorked, totalPrice)

        // Save the entry to Firebase Realtime Database
        val entryKey = dbRef2.push().key
        entryKey?.let {
            dbRef2.child(it).setValue(entry)
                .addOnSuccessListener {
                    Toast.makeText(context, "Timesheet entry saved successfully", Toast.LENGTH_SHORT).show()
                    clearFields()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to save timesheet entry", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun calculateHoursWorked(startTime: String, endTime: String): Double {
        val startHour = startTime.substringBefore(":").toDoubleOrNull() ?: 0.0
        val startMinute = startTime.substringAfter(":").toDoubleOrNull() ?: 0.0
        val endHour = endTime.substringBefore(":").toDoubleOrNull() ?: 0.0
        val endMinute = endTime.substringAfter(":").toDoubleOrNull() ?: 0.0

        val startMinutes = startHour * 60 + startMinute
        val endMinutes = endHour * 60 + endMinute

        return (endMinutes - startMinutes) / 60.0
    }

    private fun formatDate(year: Int, month: Int, dayOfMonth: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun formatTime(hourOfDay: Int, minute: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val timeFormat = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
        return timeFormat.format(calendar.time)
    }

    private fun clearFields() {
        editTextDescription.text.clear()
        editPrice.text.clear()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            val selectedImageUri = data?.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedImageUri)
                binding.imageViewPhoto.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap?
            if (imageBitmap != null) {
                binding.imageViewPhoto.setImageBitmap(imageBitmap)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


