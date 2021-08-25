package com.example.todo.ui.add

import android.app.AlertDialog
import android.content.ContentProvider
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.room.RoomDatabase
import com.example.todo.R
import com.example.todo.data.model.ToDo
import com.example.todo.data.room.MyRoomDatabase
import com.example.todo.databinding.FragmentAddBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AddFragment : Fragment() {


    private lateinit var addViewModel: AddViewModel
    private var _binding: FragmentAddBinding? = null

    var taskName: String? = null
    var discription: String? = null
    var category: String? = null
    var date: String? = null
    var time: String? = null
    var important: Boolean = false
    var reminde: Boolean = false

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        val dataSource = MyRoomDatabase.getDatabase(requireContext())
        val factory = AddViewModelFactory(dataSource.dao)
        addViewModel = ViewModelProvider(this, factory).get(AddViewModel::class.java)
        val root: View = binding.root

        loadView()

        return root
    }

    fun loadView() {
        //spinner
        val spinner = binding.spinnerCategory
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.category, R.layout.spinner_item
        )
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(0, true)
        binding.spinnerCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        0 -> category = "Home"
                        1 -> category = "Work"
                        2 -> category = "Personal"
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }

        //data
        binding.btnDate.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            val datePicker = DatePicker(requireContext())

            builder.setTitle("Choose Data")
            builder.setView(datePicker)
            builder.setNegativeButton("Cancel", null)
            builder.setPositiveButton(
                R.string.ok,
                DialogInterface.OnClickListener { dialog, id ->
                    binding.btnDate.text =
                        "${datePicker.dayOfMonth}.${datePicker.month}.${datePicker.year}"
                })
            builder.show()
        }

        //time
        binding.btnTime.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            val timePicker = TimePicker(requireContext())
//            timePicker.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.black_rock_900))

            builder.setView(timePicker)
            builder.setNegativeButton("Cancel", null)
            builder.setPositiveButton(R.string.ok, DialogInterface.OnClickListener { dialog, id ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.btnTime.text = "${timePicker.hour}:${timePicker.minute}"
                } else {
                    binding.btnTime.text = "${timePicker.currentHour}:${timePicker.currentHour}"
                }
            })
            builder.show()
        }

        //important
        binding.tvImportant.setOnClickListener {
            if(important){
                binding.tvImportant.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_star_border_24
                    ), null, null, null
                )
                important = false
            } else{
                binding.tvImportant.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_star_24
                    ), null, null, null
                )
                important = true
            }
        }

        //reminde
        binding.tvReminde.setOnClickListener {
            if(reminde){
                binding.tvReminde.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_notifications_none_24
                    ), null, null, null
                )
                reminde = false
            } else{
                binding.tvReminde.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_notifications_24
                    ), null, null, null
                )
                reminde = true
            }
        }

        //confirm
        binding.btnConfirm.setOnClickListener { confirm() }
    }

    fun confirm() {
        if (!binding.etTaskName.text.isNullOrEmpty()) {
            if (!binding.etDiscription.text.isNullOrEmpty()) {
                val todo = ToDo(
                    binding.etTaskName.text.toString(),
                    binding.etDiscription.text.toString(),
                    category,
                    binding.btnDate.text.toString(),
                    binding.btnTime.text.toString(),
                    important,
                    reminde
                )
                addViewModel.insertToDo(todo)
                findNavController().navigate(R.id.navigation_task)
            }
        }
    }
}