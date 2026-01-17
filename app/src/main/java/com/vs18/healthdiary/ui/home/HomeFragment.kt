package com.vs18.healthdiary.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vs18.healthdiary.R
import com.vs18.healthdiary.databinding.FragmentHomeBinding
import com.vs18.healthdiary.viewmodel.HealthViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HealthViewModel by activityViewModels()
    private lateinit var adapter: HealthAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = HealthAdapter(
            onEdit = { entry ->
                val bundle = Bundle().apply {
                    putLong("entryId", entry.id)
                }
                findNavController().navigate(R.id.inputFragment, bundle)
            },
            onDelete = { entry ->
                viewModel.delete(entry)
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.allEntries.collect { entries ->
                adapter.submitList(entries.sortedByDescending { it.date })
                binding.textNoData.visibility = if (entries.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}