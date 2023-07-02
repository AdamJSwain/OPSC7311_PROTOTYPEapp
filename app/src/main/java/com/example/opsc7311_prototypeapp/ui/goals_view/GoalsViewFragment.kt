package com.example.opsc7311_prototypeapp.ui.goals_view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.opsc7311_prototypeapp.databinding.FragmentGraphBinding
import com.example.opsc7311_prototypeapp.ui.home.HomeViewModel

class GoalsViewFragment : Fragment() {

    //all identities called
    private var _binding: FragmentGraphBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentGraphBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }
}