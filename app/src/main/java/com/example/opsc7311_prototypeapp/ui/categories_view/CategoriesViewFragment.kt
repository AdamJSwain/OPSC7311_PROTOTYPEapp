package com.example.opsc7311_prototypeapp.ui.categories_view

//all imports required
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.opsc7311_prototypeapp.Category
import com.example.opsc7311_prototypeapp.Worker
import com.example.opsc7311_prototypeapp.databinding.FragmentCategoriesViewBinding
import java.text.SimpleDateFormat
import java.util.*

//this fragment is for viewing all the categories the user has entered
class CategoriesViewFragment : Fragment() {

    private var _binding: FragmentCategoriesViewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesViewBinding.inflate(inflater, container, false)
        val startText = binding.editTextStartDate
        val endText = binding.editTextEndDate
        val adapter: ArrayAdapter<*>
        startText.text = SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())
        endText.text = SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())

        var cal = Calendar.getInstance()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "yyyy.MM.dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
                startText.text = sdf.format(cal.time)

            }
        val dateSetListener1 =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "yyyy.MM.dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.getDefault())

                endText.text = sdf.format((cal.time))
            }

        startText.setOnClickListener {
            DatePickerDialog(
                requireContext(), dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        endText.setOnClickListener {
            DatePickerDialog(
                requireContext(), dateSetListener1,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        binding.buttonShowCategories.setOnClickListener()
        {
            Worker.SortObjects()

            binding.listViewCategories.adapter = ArrayAdapter<Category>(requireContext(), android.R.layout.simple_list_item_1, Worker.updatedObjects)
        }

        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}