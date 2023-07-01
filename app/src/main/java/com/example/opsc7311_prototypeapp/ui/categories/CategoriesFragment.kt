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
    private lateinit var dbRef: DatabaseReference
    val database = FirebaseDatabase.getInstance("https://opsc7311-prototypeapp-default-rtdb.europe-west1.firebasedatabase.app")
    val catRef = database.getReference("Category")
    var list = mutableListOf(String())
    var adapter: ArrayAdapter<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fetchDataButBetter(Worker.userInfo)
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        val view = binding.root
        val spinner = binding.ListCategory

        val etCategoryName = view.findViewById<EditText>(R.id.editTextCategoryName)
        buttonCreateCategory = view.findViewById(R.id.buttonCreateCategory)

        adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, list)
        spinner.adapter

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
    fun fetchdata() {
       catRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (mydata in snapshot.children) list.add(mydata.value.toString())
                adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
    fun fetchDataButBetter(userName: String)
    {
        catRef.child("Categories").orderByChild(userName).get().addOnSuccessListener {
            list.add(it.toString())
        }

    }


    data class Category(val name: String, val timestamp: Long = Calendar.getInstance().timeInMillis)
}