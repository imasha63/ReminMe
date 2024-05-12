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

    //insert task
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

    fun deleteTask(task: Task) = MutableLiveData<Resource<Int>>().apply {
        postValue(Loading())
        try{
            CoroutineScope(Dispatchers.IO).launch {
                val result = taskDao.deleteTask(task)
                postValue(Success(result))
            }
        }catch (e:Exception){
            postValue(Error(e.message.toString()))
        }
    }

    // second way by Id of the task
    fun deleteTaskUsingId(taskId: String) = MutableLiveData<Resource<Int>>().apply{
        postValue(Loading())
        try{
            CoroutineScope(Dispatchers.IO).launch {
                val result = taskDao.deleteTaskUsingId(taskId)
                postValue(Success(result))
            }
        }catch (e:Exception){
            postValue(Error(e.message.toString()))
        }
    }

    fun updateTask(task: Task) = MutableLiveData<Resource<Int>>().apply {
        postValue(Loading())
        try{
            CoroutineScope(Dispatchers.IO).launch {
                val result = taskDao.updateTask(task)
                postValue(Success(result))
            }
        }catch (e:Exception){
            postValue(Error(e.message.toString()))
        }
    }

}