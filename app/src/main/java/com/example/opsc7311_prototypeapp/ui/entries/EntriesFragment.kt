package com.example.opsc7311_prototypeapp.ui.entries

//All the imports needed to make the page run
import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.opsc7311_prototypeapp.databinding.FragmentEntriesBinding
import android.graphics.Bitmap
import java.util.*
import android.content.Intent
import android.provider.MediaStore
import android.widget.*
import com.example.opsc7311_prototypeapp.*
import java.util.concurrent.TimeUnit

class EntriesFragment : Fragment() {

    //All the identities called
    private var _binding: FragmentEntriesBinding? = null
    private val binding get() = _binding!!
    private lateinit var datePicker: DatePicker
    private lateinit var timePickerStartTime: TimePicker
    private lateinit var timePickerEndTime: TimePicker
    private lateinit var editTextDescription: EditText
    private lateinit var editTextCategory: EditText
    private lateinit var buttonAddPhoto: Button
    private lateinit var buttonSaveEntry: Button
    private lateinit var imageViewPhoto: ImageView
    private var selectedImage: Bitmap? = null



    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }

    //Create the functiosn for the entries page
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_entries, container, false)

        datePicker = view.findViewById(R.id.datePicker)
        timePickerStartTime = view.findViewById(R.id.timePickerStartTime)
        timePickerEndTime = view.findViewById(R.id.timePickerEndTime)
        editTextCategory = view.findViewById(R.id.editTextCategory)
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

    //this will allow the image taken by the user to be displayed in the imageView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        imageViewPhoto = view.findViewById(R.id.imageViewPhoto)


    }

    //This will allow the user to use their camera and add an image to the entries
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


    //the method is used to capture the user information
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

        val differenceInMillis: Long = selectedEndTime.time - selectedStartTime.time
        val differenceInHours: Long = TimeUnit.MILLISECONDS.toHours(differenceInMillis)

        Toast.makeText(requireContext(), "The difference in time is: "+differenceInHours, Toast.LENGTH_SHORT).show()

        //this will allow the category enetered by the user to be seen in the view and category view page
        var categoryName: String
        categoryName = editTextCategory.text.toString()

        // This will add the entries to the timesheet class to be stored
        val entry = TimeSheetEntry(
            startDate = selectedDate,
            startTime = selectedStartTime,
            endTime = selectedEndTime,
            category = categoryName,
            description = description,
            //image = selectedImage
        )

        //Calculate the difference in the hours
        val categoryEntry = Category(categoryName, differenceInHours.toInt())


        // Add the entry to the worker class
        Worker.objectList.add(entry)
        Worker.catList.add(categoryEntry)

        // Clear the input fields or perform any desired action
        editTextCategory.text.clear()
        editTextDescription.text.clear()


    }





}

