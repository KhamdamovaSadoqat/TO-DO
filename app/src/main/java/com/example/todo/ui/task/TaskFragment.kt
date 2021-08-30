package com.example.todo.ui.task

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.data.model.ToDo
import com.example.todo.data.repository.ToDoRepository
import com.example.todo.data.room.MyRoomDatabase
import com.example.todo.data.room.ToDoDao
import com.example.todo.databinding.DialogDetailsBinding
import com.example.todo.databinding.FragmentTaskBinding
import kotlinx.coroutines.DelicateCoroutinesApi

class TaskFragment : Fragment() {

    private lateinit var taskViewModel: TaskViewModel
    private var _binding: FragmentTaskBinding? = null
    private lateinit var adapter: TaskAdapter
    private lateinit var dialogBinding: DialogDetailsBinding
    private var dialog: AlertDialog? = null
    private val binding get() = _binding!!

    @DelicateCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dataSource = MyRoomDatabase.getDatabase(requireContext())
        val factory = TaskViewModelFactory(dataSource.dao)
        taskViewModel =
            ViewModelProvider(this, factory).get(TaskViewModel::class.java)
        dialogBinding = DialogDetailsBinding.inflate(LayoutInflater.from(requireContext()))
        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        val root: View = binding.root

        loadView(dataSource.dao)

        return root
    }

    @DelicateCoroutinesApi
    fun loadView(dataSource: ToDoDao) {
        setRv(dataSource)
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_task_to_addFragment)
        }
        taskViewModel.getTodo()?.observe(viewLifecycleOwner) { todo ->
            if(!todo.isNullOrEmpty()) binding.tvAddSomeItem.visibility = View.GONE
            else binding.tvAddSomeItem.visibility = View.VISIBLE
            adapter.updateTodo(todo as ArrayList<ToDo>)
        }
    }

    @DelicateCoroutinesApi
    fun setRv(dataSource: ToDoDao) {
        adapter = TaskAdapter {
            dialogBinding.apply {
                tvTime.text = it.time
                tvDate.text = it.date
                tvDiscription.text = it.discription
                when (it.category) {
                    "Home" -> {
                        dialogBinding.tvCategory.setText(R.string.home)
                        dialogBinding.tvCategory.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.lochmara_800
                            )
                        )
                        dialogBinding.tvCategory.setBackgroundResource(
                            R.drawable.bg_task_home
                        )
                    }
                    "Work" -> {
                        dialogBinding.tvCategory.setText(R.string.work)
                        dialogBinding.tvCategory.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.medium_carmine_800
                            )
                        )
                        dialogBinding.tvCategory.setBackgroundResource(
                            R.drawable.bg_task_work
                        )
                    }
                    "Personal" -> {
                        dialogBinding.tvCategory.setText(R.string.personal)
                        dialogBinding.tvCategory.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.sea_green_700
                            )
                        )
                        dialogBinding.tvCategory.setBackgroundResource(
                            R.drawable.bg_task_personal
                        )
                    }
                }
                if (it.important) fbStar.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_star_24
                    )
                )
                else fbStar.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_star_border_24
                    )
                )
            }
            dismissDialog()
            if (dialogBinding.root.parent != null) {
                (dialogBinding.root.parent as ViewGroup).removeView(dialogBinding.root)
            }
            showDialog()

        }
        binding.rvTodo.adapter = adapter
        binding.rvTodo.layoutManager = LinearLayoutManager(requireContext())

        val swipeHandler = object : SwipeToDeleteCallback(requireContext(), dataSource) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                super.onSwiped(viewHolder, direction)
                val adapter = binding.rvTodo.adapter as TaskAdapter
                val id = adapter.removeAt(viewHolder.adapterPosition)
                Log.d("-------------", "onSwiped: id: $id")
                super.getTodo(id)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.rvTodo)
    }

    fun showDialog() {
        dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(true)
            .show()
    }

    fun dismissDialog(){
        if (dialog != null) {
            dialog!!.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}