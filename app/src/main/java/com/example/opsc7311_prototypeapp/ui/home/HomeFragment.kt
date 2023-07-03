package com.example.opsc7311_prototypeapp.ui.home

//imports required
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.opsc7311_prototypeapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    //all identities called
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var chronometer: Chronometer
    private lateinit var startButton: Button
    private lateinit var stopButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        chronometer = binding.stopwatchText
        startButton = binding.startButton
        stopButton = binding.stopButton

        startButton.setOnClickListener {
            val elapsedTime = SystemClock.elapsedRealtime() - chronometer.base
            val hours = (elapsedTime / 3600000).toInt() // Convert milliseconds to hours
            val minutes = (elapsedTime % 3600000 / 60000).toInt() // Convert remaining milliseconds to minutes
            val seconds = (elapsedTime % 60000 / 1000).toInt() // Convert remaining milliseconds to seconds

            val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            chronometer.text = timeString
            chronometer.base = SystemClock.elapsedRealtime()
            chronometer.start()
        }

        stopButton.setOnClickListener {
            chronometer.stop()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}