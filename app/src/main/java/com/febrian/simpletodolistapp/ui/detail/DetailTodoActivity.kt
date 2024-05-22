package com.febrian.simpletodolistapp.ui.detail

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.febrian.simpletodolistapp.databinding.ActivityDetailTodoBinding
import com.febrian.simpletodolistapp.service.AlarmReceiver
import com.febrian.simpletodolistapp.ui.add.DatePickerFragment
import com.febrian.simpletodolistapp.ui.add.TimePickerFragment
import com.febrian.simpletodolistapp.utils.TODO_ID
import com.febrian.simpletodolistapp.utils.showSnackBar
import com.febrian.simpletodolistapp.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class DetailTodoActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener,
    TimePickerFragment.DialogTimeListener {

    private lateinit var binding: ActivityDetailTodoBinding
    private val viewModel: DetailTodoViewModel by viewModels()

    private lateinit var alarmReceiver: AlarmReceiver

    private var id: Int = 0
    private var isCompleted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alarmReceiver = AlarmReceiver()

        id = intent.getIntExtra(TODO_ID, 0)

        viewModel.getTodoById(id)
        viewModel.todo.observe(this) { todo ->
            binding.apply {
                editEdName.setText(todo.name)
                tvDueDate.text = todo.dueDate
                tvDueTime.text = todo.dueTime
            }

            isCompleted = todo.isCompleted
        }

        binding.btnEditTodo.setOnClickListener {
            editTodo(it)
        }

        binding.datePicker.setOnClickListener {
            showDatePicker()
        }

        binding.timePicker.setOnClickListener {
            showTimePicker()
        }

        binding.back.setOnClickListener {
            finish()
        }
    }

    private fun editTodo(view: View) {

        val name = binding.editEdName.text.toString()
        val date = binding.tvDueDate.text.toString()
        val time = binding.tvDueTime.text.toString()

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

        viewModel.updateTodo(id, name, date, time, isCompleted)

        alarmReceiver.cancelAlarm(
            this,
            id
        )

        alarmReceiver.setOneTimeAlarm(
            this,
            id,
            binding.tvDueDate.text.toString(),
            binding.tvDueTime.text.toString(),
            binding.editEdName.text.toString()
        )

        view.showSnackBar("Success Edit Todo")

        Handler(Looper.getMainLooper()).postDelayed({ finish() }, 1000)
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
        binding.tvDueDate.text = dateFormat.format(calendar.time)

    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        binding.tvDueTime.text = dateFormat.format(calendar.time)

    }

    companion object {
        private const val DATE_PICKER_TAG = "DatePicker"
        private const val TIME_PICKER_TAG = "TimePicker"
    }
}