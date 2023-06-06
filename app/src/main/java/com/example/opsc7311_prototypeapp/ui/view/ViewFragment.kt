package com.example.opsc7311_prototypeapp.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.opsc7311_prototypeapp.TimeSheetEntry
import com.example.opsc7311_prototypeapp.Worker
import com.example.opsc7311_prototypeapp.databinding.FragmentViewBinding

class ViewFragment : Fragment() {

    private var _binding: FragmentViewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val worker = Worker()

        val adapter: ArrayAdapter<*>

        worker.objectList.add(TimeSheetEntry("PROG", "Programming", "2023-01-01", "2pm", "3pm"))
        worker.objectList.add(TimeSheetEntry("OPSC", "Open Source Coding", "2023-02-02", "2pm", "3pm"))
        worker.objectList.add(TimeSheetEntry("INRS", "Introduction to Research", "2023-01-01", "2pm", "3pm"))

        _binding = FragmentViewBinding.inflate(inflater, container, false)

        binding.buttonSelectDate.setOnClickListener()
        {
            binding.listViewEntries.adapter = ArrayAdapter<TimeSheetEntry>(requireContext(), android.R.layout.simple_list_item_1, worker.objectList)
        }
        val root: View = binding.root

        return root


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}