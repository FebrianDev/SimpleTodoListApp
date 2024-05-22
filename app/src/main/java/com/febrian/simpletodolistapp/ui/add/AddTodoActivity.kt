package com.febrian.simpletodolistapp.ui.add

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.febrian.simpletodolistapp.data.Todo
import com.febrian.simpletodolistapp.databinding.ActivityAddTodoBinding
import com.febrian.simpletodolistapp.service.AlarmReceiver
import com.febrian.simpletodolistapp.utils.showSnackBar
import com.febrian.simpletodolistapp.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@AndroidEntryPoint
class AddTodoActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener,
    TimePickerFragment.DialogTimeListener {

    private lateinit var binding: ActivityAddTodoBinding
    private val viewModel: AddTodoViewModel by viewModels()

    private lateinit var alarmReceiver: AlarmReceiver

    private var date: String = ""
    private var time: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alarmReceiver = AlarmReceiver()

        binding.datePicker.setOnClickListener {
            showDatePicker()
        }

        binding.timePicker.setOnClickListener {
            showTimePicker()
        }

        binding.btnAddTodo.setOnClickListener {
            addTodo(it)
        }

        binding.back.setOnClickListener {
            finish()
        }

    }

    private fun addTodo(view: View) {

        val name = binding.addEdName.text.toString()

        if (name.isEmpty()) {
            showToast("Name must be filled")
            return
        }

        if (date.isEmpty()) {
            showToast("Due date must be filled")
            return
        }

        if (time.isEmpty()) {
            showToast("Due time must be filled")
            return
        }

        val todo = Todo(
            name = name,
            dueDate = date,
            dueTime = time,
            isCompleted = false
        )

        viewModel.addTodo(todo)

        viewModel.todoId.observe(this) {

            alarmReceiver.setOneTimeAlarm(
                this,
                it.toInt(),
                binding.tvDueDate.text.toString(),
                binding.tvDueTime.text.toString(),
                binding.addEdName.text.toString()
            )

            view.showSnackBar("Success Add Todo")

            Handler(Looper.getMainLooper()).postDelayed({ finish() }, 1000)
        }
    }

    private fun showDatePicker() {
        val dialogFragment = DatePickerFragment()
        dialogFragment.show(supportFragmentManager, TIME_PICKER_TAG)
    }

    private fun showTimePicker() {
        val dialogFragment = TimePickerFragment()
        dialogFragment.show(supportFragmentManager, DATE_PICKER_TAG)
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        date = dateFormat.format(calendar.time)
        binding.tvDueDate.text = date
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        time = dateFormat.format(calendar.time)
        binding.tvDueTime.text = time
    }

    companion object {
        private const val DATE_PICKER_TAG = "DatePicker"
        private const val TIME_PICKER_TAG = "TimePicker"
    }
}