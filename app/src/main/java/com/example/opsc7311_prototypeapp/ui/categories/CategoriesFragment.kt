package com.example.opsc7311_prototypeapp.ui.categories

//All imports required
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.opsc7311_prototypeapp.R
import com.example.opsc7311_prototypeapp.Worker
import com.example.opsc7311_prototypeapp.databinding.FragmentCategoriesBinding
import com.google.firebase.database.*
import java.util.*

class CategoriesFragment : Fragment() {
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var editTextCategoryName: EditText
    private lateinit var buttonCreateCategory: Button
    val database = FirebaseDatabase.getInstance("https://opsc7311-prototypeapp-default-rtdb.europe-west1.firebasedatabase.app")
    val catRef = database.getReference("Category")
    var list = mutableListOf(String())
    var adapter: ArrayAdapter<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        val view = binding.root
        val spinner = binding.ListCategory
        val userID = Worker.userInfo

        retrieveCategoriesByUserID(userID) { categoryNames ->

            for (categoryName in categoryNames) {
                val categorySpinner = binding.ListCategory
                val categoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryNames)
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                categorySpinner.adapter = categoryAdapter
            }
        }
        val etCategoryName = view.findViewById<EditText>(R.id.editTextCategoryName)
        buttonCreateCategory = view.findViewById(R.id.buttonCreateCategory)

        adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, list)
        spinner.adapter

        buttonCreateCategory.setOnClickListener {
            var categoryName = etCategoryName.text.toString()
            if (categoryName.isNotEmpty()) {
                createCategory(categoryName)
                retrieveCategoriesByUserID(userID) { categoryNames ->

                    for (categoryName in categoryNames) {
                        val categorySpinner = binding.ListCategory
                        val categoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryNames)
                        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        categorySpinner.adapter = categoryAdapter
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please enter a category name", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun createCategory(categoryName: String) {
        //val category = Category(categoryName)
         val Category = mapOf(
             "Name" to categoryName,
         "User ID" to Worker.userInfo)
        catRef.push().setValue(Category)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun retrieveCategoriesByUserID(userID: String, callback: (List<String>) -> Unit) {


        val query = catRef.orderByChild("User ID").equalTo(userID)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val categoryNames = mutableListOf<String>()

                for (snapshot in dataSnapshot.children) {
                    val categoryName = snapshot.child("Name").value as? String
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

    data class Category(val name: String, val timestamp: Long = Calendar.getInstance().timeInMillis)
}