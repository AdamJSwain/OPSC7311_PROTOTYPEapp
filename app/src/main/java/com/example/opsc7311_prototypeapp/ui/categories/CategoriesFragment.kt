package com.example.opsc7311_prototypeapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.opsc7311_prototypeapp.R
import com.example.opsc7311_prototypeapp.databinding.FragmentCategoriesBinding

class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var spinnerCategoryColor: Spinner
    private lateinit var buttonCreateCategory: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.fragment_categories, container, false)

        spinnerCategoryColor = rootView.findViewById(R.id.spinnerCategoryColor)
        buttonCreateCategory = rootView.findViewById(R.id.buttonCreateCategory)

        // Get the array of colors from resources
        val colorsArray = resources.getStringArray(R.array.category_colors)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, colorsArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategoryColor.adapter = adapter

        // Add a listener to the button
        buttonCreateCategory.setOnClickListener {
            val selectedColor = spinnerCategoryColor.selectedItem.toString()
            //val categoryName = editTextCategoryName.text.toString()
            // Perform the action with the selected color and category name
        }

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}