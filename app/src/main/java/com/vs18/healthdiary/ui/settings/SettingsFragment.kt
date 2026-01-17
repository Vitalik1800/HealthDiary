package com.vs18.healthdiary.ui.settings

import android.content.res.Configuration
import android.os.*
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.*
import com.vs18.healthdiary.databinding.FragmentSettingsBinding
import com.vs18.healthdiary.viewmodel.HealthViewModel
import kotlinx.coroutines.*
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HealthViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonExport.setOnClickListener { exportToCsv() }

        val isDark = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        binding.switchTheme.isChecked = isDark

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val languages = arrayOf("uk", "en")
        val currentLang = Locale.getDefault().language
        val selectedIndex = if (currentLang == "uk") 0 else 1
        binding.spinnerLanguage.setSelection(selectedIndex)

        binding.spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val newLang = languages[p2]
                if (newLang != Locale.getDefault().language) {
                    val locale = Locale(newLang)
                    Locale.setDefault(locale)
                    val config = Configuration(resources.configuration)
                    config.setLocale(locale)
                    requireContext().resources.updateConfiguration(config, requireContext().resources.displayMetrics)
                    requireActivity().recreate()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun exportToCsv() {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.allEntries.value.let { entries ->
                if (entries.isEmpty()) return@let

                val sb = StringBuilder()
                sb.append("Дата,Сон (год),Настрій,Кроки,Вага (кг)\n")

                entries.sortedBy { it.date }.forEach { entry ->
                    val date = LocalDate.ofEpochDay(entry.date).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                    val weight = entry.weight ?: ""
                    sb.append("$date,${entry.sleepHours},${entry.mood},${entry.steps},$weight\n")
                }

                val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Щоденник_здоровя_${System.currentTimeMillis()}.csv")
                file.writeText(sb.toString(), Charsets.UTF_8)

                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(requireContext(), "Експортовано в Downloads", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}