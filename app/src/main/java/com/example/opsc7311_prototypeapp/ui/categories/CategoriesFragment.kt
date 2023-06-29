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
import com.example.opsc7311_prototypeapp.Worker
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
    val database = FirebaseDatabase.getInstance("https://opsc7311-prototypeapp-default-rtdb.europe-west1.firebasedatabase.app")
    val catRef = database.getReference("Category")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        val view = binding.root

        val etCategoryName = view.findViewById<EditText>(R.id.editTextCategoryName)
        buttonCreateCategory = view.findViewById(R.id.buttonCreateCategory)


        buttonCreateCategory.setOnClickListener {
            var categoryName = etCategoryName.text.toString()
            if (categoryName.isNotEmpty()) {
                createCategory(categoryName)
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

    data class Category(val name: String, val timestamp: Long = Calendar.getInstance().timeInMillis)
}