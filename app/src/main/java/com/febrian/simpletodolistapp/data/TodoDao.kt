package com.febrian.simpletodolistapp.data

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo WHERE id = :id")
    fun getTodoById(id: Int): LiveData<Todo>

    @Insert
    suspend fun insertTodo(todo: Todo): Long

    @Query("SELECT * FROM todo")
    fun getTodos(): PagingSource<Int, Todo>

    @Query("SELECT * FROM todo WHERE name LIKE '%' || :query || '%'")
    fun getTodosBySearch(query: String): PagingSource<Int, Todo>

    @Delete
    suspend fun deleteTodo(todo: Todo)

    @Query("UPDATE todo SET isCompleted = :completed WHERE id = :id")
    suspend fun updateCompleted(id: Int, completed: Boolean)

    @Query("UPDATE todo SET name = :name, dueDate = :dueDate, dueTime = :dueTime, isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateTodo(
        id: Int,
        name: String,
        dueDate: String,
        dueTime: String,
        isCompleted: Boolean
    )
}