package com.example.opsc7311_prototypeapp.ui.entries

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.opsc7311_prototypeapp.R
import com.example.opsc7311_prototypeapp.databinding.FragmentEntriesBinding
import android.graphics.Bitmap
import com.example.opsc7311_prototypeapp.TimeSheetEntry
import java.util.*
import android.content.Intent
import android.provider.MediaStore
import android.widget.*

class EntriesFragment : Fragment() {

    private var _binding: FragmentEntriesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var spinnerCategory: Spinner

    private lateinit var datePicker: DatePicker
    private lateinit var timePickerStartTime: TimePicker
    private lateinit var timePickerEndTime: TimePicker
    private lateinit var editTextDescription: EditText
    private lateinit var buttonAddPhoto: Button
    private lateinit var buttonSaveEntry: Button
    private lateinit var imageViewPhoto: ImageView
    private var selectedImage: Bitmap? = null

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_entries, container, false)

        datePicker = view.findViewById(R.id.datePicker)
        timePickerStartTime = view.findViewById(R.id.timePickerStartTime)
        timePickerEndTime = view.findViewById(R.id.timePickerEndTime)
        spinnerCategory = view.findViewById(R.id.spinnerCategory)
        editTextDescription = view.findViewById(R.id.editTextDescription)
        buttonAddPhoto = view.findViewById(R.id.buttonAddPhoto)
        buttonSaveEntry = view.findViewById(R.id.buttonSaveEntry)

        buttonAddPhoto.setOnClickListener { dispatchTakePictureIntent() }
        buttonSaveEntry.setOnClickListener { submitTimesheetEntry() }
        return view
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageViewPhoto = view.findViewById(R.id.imageViewPhoto)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap?
            selectedImage = imageBitmap

            if (selectedImage != null) {
                imageViewPhoto.visibility = View.VISIBLE
                imageViewPhoto.setImageBitmap(selectedImage)
            } else {
                imageViewPhoto.visibility = View.GONE
                imageViewPhoto.setImageBitmap(null)
            }
        }
    }

    private fun submitTimesheetEntry() {

        val description = editTextDescription.text.toString()

        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.set(datePicker.year, datePicker.month, datePicker.dayOfMonth)
        val selectedDate = selectedCalendar.time

        selectedCalendar.set(Calendar.HOUR_OF_DAY, timePickerStartTime.hour)
        selectedCalendar.set(Calendar.MINUTE, timePickerStartTime.minute)
        val selectedStartTime = selectedCalendar.time

        selectedCalendar.set(Calendar.HOUR_OF_DAY, timePickerEndTime.hour)
        selectedCalendar.set(Calendar.MINUTE, timePickerEndTime.minute)
        val selectedEndTime = selectedCalendar.time

        val entry = TimeSheetEntry(
            startDate = selectedDate,
            startTime = selectedStartTime,
            endTime = selectedEndTime,
            category = spinnerCategory,
            description = description,
            image = selectedImage
        )

        // Add the entry to the worker class
        com.example.opsc7311_prototypeapp.Worker().addTimesheetEntry(entry)

        // Clear the input fields or perform any desired actions

        editTextDescription.text.clear()


    }
}

