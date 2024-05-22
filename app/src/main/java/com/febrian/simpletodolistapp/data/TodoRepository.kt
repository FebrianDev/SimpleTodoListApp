package com.febrian.simpletodolistapp.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import javax.inject.Inject

class TodoRepository @Inject constructor(private val todoDao: TodoDao) {

    fun getTodos(query: String): Pager<Int, Todo> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                if (query.isEmpty()) todoDao.getTodos() else todoDao.getTodosBySearch(
                    query
                )
            }
        )
    }

    fun getTodoById(id: Int): LiveData<Todo> {
        return todoDao.getTodoById(id)
    }

    suspend fun insertTodo(newTodo: Todo): Long {
        return todoDao.insertTodo(newTodo)
    }

    suspend fun deleteTodo(todo: Todo) {
        todoDao.deleteTodo(todo)
    }

    suspend fun completeTodo(todo: Todo, isCompleted: Boolean) {
        todoDao.updateCompleted(todo.id, isCompleted)
    }

    suspend fun updateTodo(
        id: Int,
        name: String,
        dueDate: String,
        dueTime: String,
        isCompleted: Boolean
    ) {
        todoDao.updateTodo(id, name, dueDate, dueTime, isCompleted)
    }
}