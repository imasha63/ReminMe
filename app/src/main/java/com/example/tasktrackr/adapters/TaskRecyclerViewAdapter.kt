package com.example.tasktrackr.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tasktrackr.R
import com.example.tasktrackr.models.Task
import java.text.SimpleDateFormat
import java.util.Locale

class TaskRecyclerViewAdapter :
    RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder>() {

    private val taskList: ArrayList<Task> = arrayListOf<Task>()


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTxt: TextView = itemView.findViewById(R.id.titleTxt)
        val desTxt: TextView = itemView.findViewById(R.id.descrTxt)
        val dateTxt: TextView = itemView.findViewById(R.id.dateTxt)

    }

    fun addAllTask(newTaskList: List<Task>){
        taskList.clear()
        taskList.addAll(newTaskList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskRecyclerViewAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_task_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = taskList[position]

        holder.titleTxt.text = task.title
        holder.desTxt.text = task.description

        val dateFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss a", Locale.getDefault())
        holder.dateTxt.text = dateFormat.format(task.date)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}
