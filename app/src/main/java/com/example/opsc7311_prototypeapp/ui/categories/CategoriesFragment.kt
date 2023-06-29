package com.example.opsc7311_prototypeapp.ui.categories

//All imports required
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.opsc7311_prototypeapp.R
import com.example.opsc7311_prototypeapp.databinding.FragmentCategoriesBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class CategoriesFragment : Fragment() {
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var editTextCategoryName: EditText
    private lateinit var buttonCreateCategory: Button
    private lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        val view = binding.root

        editTextCategoryName = view.findViewById(R.id.editTextCategoryName)
        buttonCreateCategory = view.findViewById(R.id.buttonCreateCategory)
        dbRef = FirebaseDatabase.getInstance().reference.child("spinnerdata")

        buttonCreateCategory.setOnClickListener {
            val categoryName = editTextCategoryName.text.toString().trim()
            if (categoryName.isNotEmpty()) {
                createCategory(categoryName)
            } else {
                Toast.makeText(requireContext(), "Please enter a category name", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun createCategory(categoryName: String) {
        val category = Category(categoryName)
        val categoryKey = dbRef.push().key
        if (categoryKey != null) {
            dbRef.child(categoryKey).setValue(category)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        editTextCategoryName.setText("")
                        Toast.makeText(requireContext(), "Category created successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Failed to create category", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(requireContext(), "Failed to generate category key", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    data class Category(val name: String, val timestamp: Long = Calendar.getInstance().timeInMillis)
}