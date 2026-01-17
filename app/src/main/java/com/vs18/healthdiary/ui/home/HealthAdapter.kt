package com.vs18.healthdiary.ui.home

import android.annotation.SuppressLint
import android.view.*
import androidx.recyclerview.widget.*
import com.vs18.healthdiary.R
import com.vs18.healthdiary.data.entity.HealthEntry
import com.vs18.healthdiary.databinding.ItemHealthBinding
import java.time.LocalDate
import java.time.format.*

class HealthAdapter(
    private val onEdit: (HealthEntry) -> Unit,
    private val onDelete: (HealthEntry) -> Unit
) : ListAdapter<HealthEntry, HealthAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(private val binding: ItemHealthBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(entry: HealthEntry, onEdit: (HealthEntry) -> Unit, onDelete: (HealthEntry) -> Unit) {
            val context = binding.root.context
            val locale = context.resources.configuration.locales[0]

            val date = LocalDate.ofEpochDay(entry.date)
            val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", locale)
            binding.textDate.text = date.format(formatter)
            binding.textDayOfWeek.text = date.dayOfWeek.getDisplayName(TextStyle.FULL, locale)

            binding.textSleep.text = "${entry.sleepHours} ${ context.getString(R.string.hours) }"
            binding.textMood.text = "${ context.getString(R.string.mood_p) } ${entry.mood}/5"
            binding.textSteps.text = "${entry.steps} ${ context.getString(R.string.steps) }"
            binding.textWeight.text = entry.weight?.let { "$it ${ context.getString(R.string.w_kg) }" } ?: "-"

            binding.buttonEdit.setOnClickListener { onEdit(entry) }
            binding.buttonDelete.setOnClickListener { onDelete(entry)  }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHealthBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onEdit, onDelete)
    }

    class DiffCallback : DiffUtil.ItemCallback<HealthEntry>() {
        override fun areItemsTheSame(oldItem: HealthEntry, newItem: HealthEntry) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: HealthEntry, newItem: HealthEntry) = oldItem == newItem
    }
}