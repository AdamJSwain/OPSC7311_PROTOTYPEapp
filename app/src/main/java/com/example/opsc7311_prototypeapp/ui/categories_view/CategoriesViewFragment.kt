package com.example.opsc7311_prototypeapp.ui.categories_view

//all imports required
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.opsc7311_prototypeapp.Category
import com.example.opsc7311_prototypeapp.Worker
import com.example.opsc7311_prototypeapp.databinding.FragmentCategoriesViewBinding

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

        val adapter: ArrayAdapter<*>

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