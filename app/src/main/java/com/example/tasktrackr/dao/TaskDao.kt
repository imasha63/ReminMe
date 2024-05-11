package com.example.tasktrackr.dao

import androidx.room.*
import com.example.tasktrackr.models.Task

@Dao
interface TaskDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

}