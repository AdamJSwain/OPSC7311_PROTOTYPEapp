package com.example.opsc7311_prototypeapp.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.opsc7311_prototypeapp.TimeSheetEntry
import com.example.opsc7311_prototypeapp.Worker
import com.example.opsc7311_prototypeapp.databinding.FragmentViewBinding
import java.sql.Date

class ViewFragment : Fragment() {

    //All identities called
    private var _binding: FragmentViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentViewBinding.inflate(inflater, container, false)
        binding.buttonSelectDate.setOnClickListener()
        {
            binding.listViewEntries.adapter = ArrayAdapter<TimeSheetEntry>(requireContext(), android.R.layout.simple_list_item_1, Worker.objectList)
        }
        val root: View = binding.root

        return root


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}