package com.example.opsc7311_prototypeapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.opsc7311_prototypeapp.R
import com.example.opsc7311_prototypeapp.databinding.FragmentCategoriesBinding


class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    // This property is only valid between onCreateView and
    // onDestroyView.

    private lateinit var ListCategory: ListView
    private lateinit var itemList: ArrayList<String>
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        ListCategory = view.findViewById(R.id.ListCategory)
        itemList = ArrayList()
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, itemList)
        ListCategory.adapter = adapter

        val buttonCreateCategory: Button = view.findViewById(R.id.buttonCreateCategory)
        buttonCreateCategory.setOnClickListener {
            val newItem = "New Item"
            addItemToList(newItem)
        }

        val ButtonUpdate: Button = view.findViewById(R.id.ButtonUpdate)
        ButtonUpdate.setOnClickListener {
            val index = 0  // Specify the index of the item to update
            val updatedItem = "Updated Item"
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