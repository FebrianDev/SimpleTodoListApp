package com.febrian.simpletodolistapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.febrian.simpletodolistapp.data.Todo
import com.febrian.simpletodolistapp.data.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailTodoViewModel @Inject constructor(private val todoRepository: TodoRepository) :
    ViewModel() {
    private val _todoId = MutableLiveData<Int>()
    private val _todo = _todoId.switchMap { id ->
        todoRepository.getTodoById(id)
    }
    val todo: LiveData<Todo> = _todo

    fun getTodoById(id: Int) {
        _todoId.value = id
    }

    fun updateTodo(
        id: Int,
        name: String,
        dueDate: String,
        dueTime: String,
        isCompleted: Boolean
    ) {
        viewModelScope.launch {
            todoRepository.updateTodo(id, name, dueDate, dueTime, isCompleted)
        }
    }
}