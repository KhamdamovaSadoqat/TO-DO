package com.example.todo.ui.task

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.data.model.ToDo
import com.example.todo.data.repository.ToDoRepository
import com.example.todo.data.room.ToDoDao
import com.example.todo.databinding.ItemTodoBinding
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

open class SwipeToDeleteCallback(context: Context, dataSource: ToDoDao) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val deleteIcon =
        ContextCompat.getDrawable(context, R.drawable.ic_baseline_delete_outline_24)
    private val intrinsicWidth = deleteIcon?.intrinsicWidth
    private val intrinsicHeight = deleteIcon?.intrinsicHeight
    private val background = ColorDrawable()
    private val backgroundColor = Color.parseColor("#f44336")
    private val repo = ToDoRepository(dataSource)
    private var id: Int? = null

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    @DelicateCoroutinesApi
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            Log.d("-------------", "onSwiped: id: $id")
            id?.let { repo.deleteTodo(it) }
        }
    }

    fun getTodo(id: Int){
        Log.d("-------------", "getTodo: id: $id")
        this.id = id
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top


        background.color = backgroundColor
        background.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )
        background.draw(c)

        val iconTop = itemView.top + (itemHeight - intrinsicHeight!!) / 2
        val iconMargin = (itemHeight - intrinsicHeight) / 2
        val iconLeft = itemView.right - iconMargin - intrinsicWidth!!
        val iconRight = itemView.right - iconMargin
        val iconBottom = iconTop + intrinsicHeight

        deleteIcon?.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        deleteIcon?.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

}