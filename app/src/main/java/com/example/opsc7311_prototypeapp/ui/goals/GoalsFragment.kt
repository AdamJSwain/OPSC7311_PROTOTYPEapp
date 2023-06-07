package com.example.opsc7311_prototypeapp.ui.goals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.opsc7311_prototypeapp.databinding.FragmentGoalsBinding

class GoalsFragment : Fragment() {

    private var _binding: FragmentGoalsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

        //when the button is press will give a toast message and save the entries
        btnSave.setOnClickListener()
        {
            Toast.makeText(requireContext(), "Your minimum goal has been set to: "+minGoal.text.toString() + " hours, and your maximum goal has been set to: "
                    + maxGoal.text.toString() + " hours",  Toast.LENGTH_SHORT).show()

        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}