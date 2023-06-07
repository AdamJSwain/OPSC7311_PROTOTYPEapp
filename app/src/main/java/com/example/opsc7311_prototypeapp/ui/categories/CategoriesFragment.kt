package com.example.opsc7311_prototypeapp.ui.categories

//All imports required
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.opsc7311_prototypeapp.R
import com.example.opsc7311_prototypeapp.ShareViewModel
import com.example.opsc7311_prototypeapp.databinding.FragmentCategoriesBinding

class CategoriesFragment : Fragment() {

    //all identities that have been called
private var _binding: FragmentCategoriesBinding? = null
private val binding get() = _binding!!
private lateinit var ListCategory: ListView
private lateinit var itemList: ArrayList<String>
private lateinit var adapter: ArrayAdapter<String>
private lateinit var editTextCategoryName:EditText
private val sharedViewModel: ShareViewModel by activityViewModels()

    //Create the functions for the categories page
override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
    val view = binding.root
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

    }


    return view
}

// Add items to the ListView
private fun addItemToList(item: String) {
    itemList.add(item)
    adapter.notifyDataSetChanged()

}
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    ListCategory = view.findViewById(R.id.ListCategory)

    sharedViewModel.selectedItem.observe(viewLifecycleOwner) { selectedItem ->
        // Update the ListView based on the selected item
        // For example, filter the list or fetch new data
        updateListView(selectedItem)
    }
}

private fun updateListView(selectedItem: String) {
    // Replace this with your logic to update the ListView based on the selected item
    val dataList = getDataForSelectedItem(selectedItem)

    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, dataList)
    ListCategory.adapter = adapter
}

private fun getDataForSelectedItem(selectedItem: String): List<String> {
    // Replace this with your logic to fetch or filter the data based on the selected item
    // For example, return a filtered list based on the selected item
    return when (selectedItem) {
        "Option 1" -> listOf("Item 1", "Item 2", "Item 3")
        "Option 2" -> listOf("Item 4", "Item 5", "Item 6")
        "Option 3" -> listOf("Item 7", "Item 8", "Item 9")
        else -> emptyList()
    }

}

}