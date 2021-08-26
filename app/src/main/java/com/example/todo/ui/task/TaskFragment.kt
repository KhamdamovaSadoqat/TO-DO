package com.example.todo.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.data.model.ToDo
import com.example.todo.data.repository.ToDoRepository
import com.example.todo.data.room.MyRoomDatabase
import com.example.todo.databinding.FragmentTaskBinding

class TaskFragment : Fragment() {

    private lateinit var taskViewModel: TaskViewModel
    private var _binding: FragmentTaskBinding? = null
    private lateinit var adapter: TaskAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dataSource = MyRoomDatabase.getDatabase(requireContext())
        val factory = TaskViewModelFactory(dataSource.dao)
        taskViewModel =
            ViewModelProvider(this, factory).get(TaskViewModel::class.java)

        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        val root: View = binding.root

        loadView()

        return root
    }

    fun loadView(){
        setRv()
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_task_to_addFragment)
        }
        taskViewModel.getTodo()?.observe(viewLifecycleOwner){todo ->
            adapter.updateTodo(todo as ArrayList<ToDo>)
        }
    }

    fun setRv(){
        adapter = TaskAdapter {  }
        binding.rvTodo.adapter = adapter
        binding.rvTodo.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}