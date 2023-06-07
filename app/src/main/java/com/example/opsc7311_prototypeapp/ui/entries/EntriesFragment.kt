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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.opsc7311_prototypeapp.ShareViewModel
import com.example.opsc7311_prototypeapp.Worker

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
    private val sharedViewModel: ShareViewModel by activityViewModels()


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

        val adapter: ArrayAdapter<String>

        val stringArray: ArrayList<String>
        Worker.stringList.add("OPSC7311")
        Worker.stringList.add("INRS7321")
        Worker.stringList.add("PROG7311")
        Worker.stringList.add("IPMA6212")

        // Populate the dropdown bar with categories
        spinnerCategory.adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, Worker.stringList)


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
        spinnerCategory = view.findViewById<Spinner>(R.id.spinnerCategory)

        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sharedViewModel.dataList)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = arrayAdapter

        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = spinnerCategory.getItemAtPosition(position).toString()
                sharedViewModel.selectedItem.value = selectedItem
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle when no item is selected
            }
        }
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
            category = "work",
            description = description,
            //image = selectedImage
        )


        // Add the entry to the worker class
        Worker.objectList.add(TimeSheetEntry("work", description, selectedDate, selectedStartTime, selectedEndTime))

        // Clear the input fields or perform any desired action

        editTextDescription.text.clear()


    }





}

