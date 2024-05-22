package com.febrian.simpletodolistapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val dueDate: String,
    val dueTime: String,
    val isCompleted: Boolean = false
)