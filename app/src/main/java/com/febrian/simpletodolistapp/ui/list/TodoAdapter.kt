package com.febrian.simpletodolistapp.ui.list

import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.febrian.simpletodolistapp.data.Todo
import com.febrian.simpletodolistapp.databinding.ItemTodoBinding
import com.febrian.simpletodolistapp.ui.detail.DetailTodoActivity
import com.febrian.simpletodolistapp.utils.TODO_ID

class TodoAdapter(
    private val onCheckedChange: (Todo, Boolean) -> Unit
) : PagingDataAdapter<Todo, TodoAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: ItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var todo: Todo

        fun bind(todo: Todo) {

            this.todo = todo

            binding.apply {

                if (todo.isCompleted) {
                    tvName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    tvDueDate.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }

                tvName.text = todo.name
                tvDueDate.text = "${todo.dueDate} : ${todo.dueTime}"
                checkbox.isChecked = todo.isCompleted
            }

            itemView.setOnClickListener {
                val detailIntent = Intent(itemView.context, DetailTodoActivity::class.java)
                detailIntent.putExtra(TODO_ID, todo.id)
                itemView.context.startActivity(detailIntent)
            }

            binding.checkbox.setOnClickListener {
                onCheckedChange(todo, !todo.isCompleted)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val Todo = getItem(position) as Todo
//        var condition = TodoTitleView.NORMAL
//        //TODO 9 : Bind data to ViewHolder (You can run app to check)
//        when {
//            //TODO 10 : Display title based on status using TitleTextView
//            Todo.isCompleted -> {
//                //DONE
//                condition = TodoTitleView.DONE
//                holder.cbComplete.isChecked = true
//            }
//
//            Todo.dueDateMillis < System.currentTimeMillis() -> {
//                //OVERDUE
//                condition = TodoTitleView.OVERDUE
//                holder.cbComplete.isChecked = false
//            }
//
//            else -> {
//                //NORMAL
//                condition = TodoTitleView.NORMAL
//                holder.cbComplete.isChecked = false
//            }
//        }
        val todo = getItem(position) as Todo
        holder.bind(todo)
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Todo>() {
            override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
                return oldItem == newItem
            }
        }

    }
}