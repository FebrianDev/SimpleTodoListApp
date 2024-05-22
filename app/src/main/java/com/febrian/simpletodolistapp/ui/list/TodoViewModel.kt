package com.febrian.simpletodolistapp.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.febrian.simpletodolistapp.data.Todo
import com.febrian.simpletodolistapp.data.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(private val todoRepository: TodoRepository) :
    ViewModel() {

    private val query = MutableLiveData<String>("")

    val todos: LiveData<PagingData<Todo>>
        get() = query.switchMap {
            todoRepository.getTodos(it).flow.cachedIn(viewModelScope).asLiveData()
        }

    fun searchTodos(q: String) {
        query.value = q
    }

    fun completeTodo(todo: Todo, completed: Boolean) {
        viewModelScope.launch {
            todoRepository.completeTodo(todo, completed)
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            todoRepository.deleteTodo(todo)
        }
    }
}