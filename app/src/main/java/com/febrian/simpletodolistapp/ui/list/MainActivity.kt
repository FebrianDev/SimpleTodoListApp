package com.febrian.simpletodolistapp.ui.list

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.febrian.simpletodolistapp.databinding.ActivityMainBinding
import com.febrian.simpletodolistapp.service.AlarmReceiver
import com.febrian.simpletodolistapp.ui.add.AddTodoActivity
import com.febrian.simpletodolistapp.utils.showDialog
import com.febrian.simpletodolistapp.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val todoViewModel: TodoViewModel by viewModels()

    private lateinit var todoAdapter: TodoAdapter

    private lateinit var alarmReceiver: AlarmReceiver

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        alarmReceiver = AlarmReceiver()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                todoViewModel.searchTodos(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                todoViewModel.searchTodos(newText)
                return true
            }
        })

        todoAdapter = TodoAdapter { todo, b ->
            todoViewModel.completeTodo(todo, b)
            if (b) {
                alarmReceiver.cancelAlarm(applicationContext, todo.id)
                showToast("Todo is completed")
            } else {
                alarmReceiver.setOneTimeAlarm(
                    applicationContext,
                    todo.id,
                    todo.dueDate,
                    todo.dueTime,
                    todo.name
                )

                showToast("Todo is active")
            }
        }

        todoAdapter.notifyDataSetChanged()

        initAction()
        getData()

        binding.addTodo.setOnClickListener {
            val addIntent = Intent(this, AddTodoActivity::class.java)
            startActivity(addIntent)
        }
    }

    private fun getData() {
        binding.rvTodo.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = todoAdapter
        }

        todoViewModel.todos.observe(this) {
            todoAdapter.submitData(lifecycle, it)
        }
    }

    private fun initAction() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(0, ItemTouchHelper.LEFT)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                showDialog("Are you sure delete this todo?", {
                    val todo = (viewHolder as TodoAdapter.ViewHolder).todo
                    todoViewModel.deleteTodo(todo)
                    alarmReceiver.cancelAlarm(applicationContext, todo.id)
                }, {
                    todoAdapter.notifyItemChanged(viewHolder.bindingAdapterPosition)
                })

            }

        })
        itemTouchHelper.attachToRecyclerView(binding.rvTodo)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifications permission rejected", Toast.LENGTH_SHORT).show()
            }
        }

}