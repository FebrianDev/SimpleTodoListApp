package com.febrian.simpletodolistapp.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febrian.simpletodolistapp.data.Todo
import com.febrian.simpletodolistapp.data.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTodoViewModel @Inject constructor(private val todoRepository: TodoRepository) :
    ViewModel() {

    private val _todoId = MutableLiveData<Long>()
    val todoId: LiveData<Long> get() = _todoId

    fun addTodo(todo: Todo) {
        viewModelScope.launch {
            _todoId.value = todoRepository.insertTodo(todo)
        }
    }
}