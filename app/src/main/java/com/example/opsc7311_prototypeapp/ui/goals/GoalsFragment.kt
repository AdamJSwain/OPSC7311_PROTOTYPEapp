package com.example.opsc7311_prototypeapp.ui.goals

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.opsc7311_prototypeapp.Worker
import com.example.opsc7311_prototypeapp.databinding.FragmentGoalsBinding
import com.github.mikephil.charting.components.LimitLine
import com.google.firebase.database.*

class GoalsFragment : Fragment() {

    private var _binding: FragmentGoalsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val database = FirebaseDatabase.getInstance("https://opsc7311-prototypeapp-default-rtdb.europe-west1.firebasedatabase.app")
    val goalRef = database.getReference("UserGoal")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //all identities call
        _binding = FragmentGoalsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val minGoal = binding.editTextMinGoal
        val maxGoal = binding.editTextMaxGoal
        val btnSave = binding.buttonSaveGoals
        val userId = goalRef.orderByChild("User ID").equalTo(Worker.userInfo)
        retrieveUserGoalsByUserID(userId) { minimumGoal, maximumGoal ->

            minGoal.hint = "Minimum goal (" + minimumGoal.toInt().toString() + ")" + "Hours"
            maxGoal.hint = "Maximum goal (" + maximumGoal.toInt().toString() + ")" + "Hours"

        }

        //when the button is press will give a toast message and save the entries
        btnSave.setOnClickListener()
        {
            updateData(Worker.userInfo, minGoal.text.toString().toInt(), maxGoal.text.toString().toInt())
        }

        return root
    }
    private fun retrieveUserGoalsByUserID(userIDQuery: Query, callback: (Float, Float) -> Unit) {
        val userGoalsRef = database.getReference("UserGoal")

        userIDQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Iterate through the snapshot to get the user ID
                val userId = snapshot.children.firstOrNull()?.key

                println("Retrieved user ID: $userId") // Print retrieved user ID

                if (userId != null) {
                    val userGoalsQuery = userGoalsRef.child(userId)

                    userGoalsQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val minimumGoal =
                                snapshot.child("Min Goal").getValue(Float::class.java) ?: 0f
                            val maximumGoal =
                                snapshot.child("Max Goal").getValue(Float::class.java) ?: 0f

                            callback(minimumGoal, maximumGoal)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Handle error
                        }
                    })
                } else {
                    println("User ID not found") // Print user ID not found
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "It broken", Toast.LENGTH_SHORT)
            }
        })
    }

    private fun updateData(username: String, minimumGoal: Int, maximumGoal: Int) {
        val userGoal = mapOf(
            "Max Goal" to maximumGoal,
            "Min Goal" to minimumGoal,
            "User ID" to Worker.userInfo)

        goalRef.child(Worker.userInfo).updateChildren(userGoal).addOnCompleteListener {
           if(it.isSuccessful){
               Toast.makeText(requireContext(), "Your minimum goal has been set to: "+minimumGoal.toString() + " hours, and your maximum goal has been set to: "
                       + maximumGoal.toString() + " hours",  Toast.LENGTH_SHORT).show()
           }
            else{
               Toast.makeText(requireContext(), it.exception?.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
           }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}