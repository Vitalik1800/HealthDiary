package com.vs18.healthdiary.ui.charts

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import com.vs18.healthdiary.viewmodel.HealthViewModel
import com.vs18.healthdiary.databinding.FragmentChartsBinding
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.vs18.healthdiary.R
import kotlinx.coroutines.launch
import java.time.LocalDate

class ChartsFragment : Fragment() {

    private var _binding: FragmentChartsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HealthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChartsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val end = LocalDate.now().toEpochDay()
        val start = end - 89

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getEntriesInRange(start, end).collect { entries ->
                if (entries.isEmpty()) {
                    binding.textNoData.visibility = View.VISIBLE
                    binding.chartSleep.visibility = View.GONE
                    binding.chartMood.visibility = View.GONE
                    binding.chartSteps.visibility = View.GONE
                    binding.chartWeight.visibility = View.GONE
                    binding.textSleep.visibility = View.GONE
                    binding.textMood.visibility = View.GONE
                    binding.textSteps.visibility = View.GONE
                    binding.textWeight.visibility = View.GONE
                } else {
                    binding.textNoData.visibility = View.GONE
                    binding.chartSleep.visibility = View.VISIBLE
                    binding.chartMood.visibility = View.VISIBLE
                    binding.chartSteps.visibility = View.VISIBLE
                    binding.chartWeight.visibility = View.VISIBLE
                    binding.textSleep.visibility = View.VISIBLE
                    binding.textMood.visibility = View.VISIBLE
                    binding.textSteps.visibility = View.VISIBLE
                    binding.textWeight.visibility = View.VISIBLE
                    val sleepEntries = entries.mapIndexed { index, entry ->
                        Entry(index.toFloat(), entry.sleepHours)
                    }
                    setupChart(
                        binding.chartSleep,
                        sleepEntries,
                        getString(R.string.sleep_hours),
                        ColorTemplate.MATERIAL_COLORS[0]
                    )

                    val moodEntries = entries.mapIndexed { index, entry ->
                        Entry(index.toFloat(), entry.mood.toFloat())
                    }
                    setupChart(
                        binding.chartMood,
                        moodEntries,
                        getString(R.string.mood),
                        ColorTemplate.MATERIAL_COLORS[1]
                    )

                    val stepEntries = entries.mapIndexed { index, entry ->
                        Entry(index.toFloat(), entry.steps.toFloat())
                    }
                    setupChart(
                        binding.chartSteps,
                        stepEntries,
                        getString(R.string.steps),
                        ColorTemplate.MATERIAL_COLORS[2]
                    )

                    val weightEntries = entries.mapIndexedNotNull { index, entry ->
                        entry.weight?.let { Entry(index.toFloat(), it) }
                    }
                    setupChart(
                        binding.chartWeight,
                        weightEntries,
                        getString(R.string.weight_kg1),
                        ColorTemplate.MATERIAL_COLORS[3]
                    )
                }
            }
        }
    }

    private fun setupChart(
        chart: com.github.mikephil.charting.charts.LineChart,
        data: List<Entry>,
        label: String,
        color: Int
    ) {
        val dataSet = LineDataSet(data, label).apply {
            this.color = color
            valueTextColor = Color.BLACK
            valueTextSize = 12f
            lineWidth = 3.5f
            setCircleColor(color)
            circleRadius = 5f
            setDrawCircleHole(false)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            cubicIntensity = 0.2f
        }

        chart.apply {
            this.data = LineData(dataSet)
            description.isEnabled = false
            legend.isEnabled = true
            xAxis.isEnabled = false
            axisRight.isEnabled = false
            axisLeft.axisMinimum = 0f
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)
            setBackgroundColor(Color.WHITE)
            animateX(800)
            invalidate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}