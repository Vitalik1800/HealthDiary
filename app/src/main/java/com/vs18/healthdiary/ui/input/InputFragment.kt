package com.vs18.healthdiary.ui.input

import android.os.Bundle
import android.view.*
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.vs18.healthdiary.R
import com.vs18.healthdiary.data.entity.HealthEntry
import com.vs18.healthdiary.viewmodel.HealthViewModel
import com.vs18.healthdiary.databinding.FragmentInputBinding
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class InputFragment : Fragment() {

    private var _binding: FragmentInputBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HealthViewModel by activityViewModels()

    private var entryId: Long = 0
    private var editingEntry: HealthEntry? = null
    private var selectedDateEpochDay: Long = LocalDate.now().toEpochDay()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        entryId = arguments?.getLong("entryId") ?: 0L

        binding.editDate.setOnClickListener {
            val picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.select_date))
                .setSelection(selectedDateEpochDay * 86_400_000L)
                .build()

            picker.addOnPositiveButtonClickListener { selection ->
                selectedDateEpochDay = selection / 86_400_000L
                val date = LocalDate.ofEpochDay(selectedDateEpochDay)
                binding.editDate.setText(
                    date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("uk")))
                )
            }
            picker.show(parentFragmentManager, "DATE_PICKER")
        }

        if (entryId != 0L) {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.allEntries.collect { list ->
                    editingEntry = list.find { it.id == entryId }
                    editingEntry?.let { entry ->
                        fillForm(entry)
                        selectedDateEpochDay = entry.date
                        binding.editDate.setText(
                            LocalDate.ofEpochDay(entry.date)
                                .format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("uk")))
                        )
                    }
                }
            }
        } else {
            binding.editDate.setText(getString(R.string.todayDay))
        }

        binding.buttonSave.setOnClickListener {
            saveEntry()
        }
    }

    private fun fillForm(entry: HealthEntry) {
        binding.editSleep.setText(entry.sleepHours.toString())
        binding.sliderMood.value = entry.mood.toFloat()
        binding.editSteps.setText(entry.steps.toString())
        entry.weight?.let { binding.editWeight.setText(it.toString()) }
    }

    private fun saveEntry() {
        val sleep = binding.editSleep.text.toString().toFloatOrNull() ?: 0f
        val mood = binding.sliderMood.value.toInt()
        val steps = binding.editSteps.text.toString().toIntOrNull() ?: 0
        val weightText = binding.editWeight.text.toString()
        val weight = if (weightText.isBlank()) null else weightText.toFloat()

        val entry = editingEntry?.copy(
            date = selectedDateEpochDay,
            sleepHours = sleep,
            mood = mood,
            steps = steps,
            weight = weight
        ) ?: HealthEntry(
            date = selectedDateEpochDay,
            sleepHours = sleep,
            mood = mood,
            steps = steps,
            weight = weight
        )

        if (editingEntry == null) {
            viewModel.insert(entry)
        } else {
            viewModel.update(entry)
        }

        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}