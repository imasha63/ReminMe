package com.example.tasktrackr.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.tasktrackr.models.Task
import com.example.tasktrackr.repository.TaskRepository
import com.example.tasktrackr.utils.Resource

class TaskViewModel (application: Application) : AndroidViewModel(application){

    private val taskRepository = TaskRepository(application)

    fun getTaskList() = taskRepository.getTaskList()

    fun insertTask(task: Task): MutableLiveData<Resource<Long>>{
        return taskRepository.insertTask(task)
    }
}