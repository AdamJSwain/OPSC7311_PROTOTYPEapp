package com.example.opsc7311_prototypeapp.ui.entries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.opsc7311_prototypeapp.R
import com.example.opsc7311_prototypeapp.Worker
import com.example.opsc7311_prototypeapp.databinding.FragmentEntriesBinding

class EntriesFragment : Fragment() {

    private var _binding: FragmentEntriesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var spinnerCategory: Spinner

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_entries, container, false)

        spinnerCategory= view.findViewById(R.id.spinnerCategory)

        val bundle = arguments
        if (bundle != null && bundle.containsKey("enteredText")) {
            val enteredText = bundle.getString("enteredText")

            // Create an ArrayAdapter with a single item (entered text)
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listOf(enteredText))
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCategory.adapter = adapter
        }

        return view
    }
}