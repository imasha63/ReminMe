package com.example.tasktrackr.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.tasktrackr.database.TaskDatabase
import com.example.tasktrackr.models.Task
import com.example.tasktrackr.utils.Resource
import com.example.tasktrackr.utils.Resource.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.flow


class TaskRepository(application: Application) {

    private val taskDao = TaskDatabase.getInstance(application).taskDao

    fun getTaskList() = flow{
        emit(Loading())
        try {
            val result = taskDao.getTaskList()
            emit(Success(result))
        }catch (e:Exception){
            emit(Error(e.message.toString()))
        }
    }

    fun insertTask(task: Task) = MutableLiveData<Resource<Long>>().apply {
        postValue(Loading())
        try{
            CoroutineScope(Dispatchers.IO).launch {
                val result = taskDao.insertTask(task)
                postValue(Success(result))
            }
        }catch (e:Exception){
            postValue(Error(e.message.toString()))
        }
    }

}