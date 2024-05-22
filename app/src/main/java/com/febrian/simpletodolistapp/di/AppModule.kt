package com.febrian.simpletodolistapp.di

import android.app.Application
import androidx.room.Room
import com.febrian.simpletodolistapp.data.TodoDao
import com.febrian.simpletodolistapp.data.TodoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): TodoDatabase =
        Room.databaseBuilder(app, TodoDatabase::class.java, "todo.db")
            .fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideTodoDao(database: TodoDatabase): TodoDao = database.todoDao()
}