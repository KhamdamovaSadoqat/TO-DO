package com.example.todo.ui.task

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.data.model.ToDo
import com.example.todo.databinding.ItemTodoBinding
import com.example.todo.utils.DataTimeUtils

class TaskAdapter(private val itemClickListener: ((ToDo) -> Unit)) :
    RecyclerView.Adapter<TaskAdapter.VH>() {

    private var list = arrayListOf<ToDo>()
    private var id: Int? = null

    fun updateTodo(list: ArrayList<ToDo>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun removeAt(position: Int): Int{
        this.id = list[position].id
        list.removeAt(position)
        notifyDataSetChanged()
        return id as Int
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTodoBinding.inflate(inflater, parent, false)
        return VH(binding, parent.context)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.setOnClickListener { itemClickListener.invoke(list[position]) }
        holder.onBind(list[position])
    }

    override fun getItemCount() = list.size
    class VH(private val binding: ItemTodoBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        val util = DataTimeUtils()
        fun onBind(todo: ToDo) {
            binding.apply {
                tvTask.text = todo.taskName
                tvDate.text = util.dateWithoutYear(todo.date)

                when (todo.category) {
                    "Home" -> {
                        binding.tvCategory.setText(R.string.home)
                        binding.tvCategory.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.lochmara_800
                            )
                        )
                        binding.tvCategory.setBackgroundResource(
                            R.drawable.bg_task_home
                        )
                    }
                    "Work" -> {
                        binding.tvCategory.setText(R.string.work)
                        binding.tvCategory.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.medium_carmine_800
                            )
                        )
                        binding.tvCategory.setBackgroundResource(
                            R.drawable.bg_task_work
                        )
                    }
                    "Personal" -> {
                        binding.tvCategory.setText(R.string.personal)
                        binding.tvCategory.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.sea_green_700
                            )
                        )
                        binding.tvCategory.setBackgroundResource(
                            R.drawable.bg_task_personal
                        )
                    }
                }

                if(todo.important) fbStar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_star_24))
                else fbStar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_star_border_24))
            }
        }
    }
}