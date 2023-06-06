package com.example.opsc7311_prototypeapp.ui.categories

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.opsc7311_prototypeapp.R
import com.example.opsc7311_prototypeapp.Worker
import com.example.opsc7311_prototypeapp.databinding.FragmentCategoriesBinding
import com.example.opsc7311_prototypeapp.ui.entries.EntriesFragment


class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    // This property is only valid between onCreateView and
    // onDestroyView.
// identites for add and updating the list view
    private lateinit var ListCategory: ListView
    private lateinit var itemList: ArrayList<String>
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var editTextCategoryName:EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        ListCategory = view.findViewById(R.id.ListCategory)
        editTextCategoryName = view.findViewById(R.id.editTextCategoryName)
        itemList = ArrayList()
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, itemList)
        ListCategory.adapter = adapter

        // adding items to the list View
        val buttonCreateCategory: Button = view.findViewById(R.id.buttonCreateCategory)
        buttonCreateCategory.setOnClickListener {

            val newItem = editTextCategoryName.text.toString()
            addItemToList(newItem)


            val entriesFragment = EntriesFragment()

            // Create a bundle and pass the entered text to it
            val bundle = Bundle()
            bundle.putString("enteredText", newItem)
            entriesFragment.arguments = bundle

            // Replace the current fragment with the DropdownFragment
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.spinnerCategory, entriesFragment)
                .addToBackStack(null)
                .commit()
        }

        //updating the list view selected
        val ButtonUpdate: Button = view.findViewById(R.id.ButtonUpdate)
        ButtonUpdate.setOnClickListener {
            val index = 0  // Specify the index of the item to update
            val updatedItem = editTextCategoryName.text.toString()
            updateItemInList(index, updatedItem)
        }



        return view
    }

    // Add items to the ListView
    private fun addItemToList(item: String) {
        itemList.add(item)
        adapter.notifyDataSetChanged()
    }

    // Update items in the ListView
    private fun updateItemInList(index: Int, updatedItem: String) {
        itemList[index] = updatedItem
        adapter.notifyDataSetChanged()
    }



}