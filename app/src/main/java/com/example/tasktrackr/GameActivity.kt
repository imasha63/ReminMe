package com.example.tasktrackr

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.example.tasktrackr.databinding.ActivityGameBinding
import com.example.tasktrackr.models.Task
import com.example.tasktrackr.utils.Status
import com.example.tasktrackr.utils.setupDialog
import com.example.tasktrackr.utils.validateEditText
import com.example.tasktrackr.viewmodels.TaskViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import androidx.lifecycle.MutableLiveData
import com.example.tasktrackr.adapters.TaskRecyclerViewAdapter
import com.example.tasktrackr.utils.clearEditText
import com.example.tasktrackr.utils.longToastShow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class GameActivity : AppCompatActivity() {

    private val gameBinding: ActivityGameBinding by lazy {
        ActivityGameBinding.inflate(layoutInflater)
    }


    private val addTaskDialog : Dialog by lazy {
        Dialog(this,R.style.DialogCustomTheme).apply {
            setupDialog(R.layout.add_task_layout)
        }
    }

    private val updateTaskDialog : Dialog by lazy {
        Dialog(this,R.style.DialogCustomTheme).apply {
            setupDialog(R.layout.update_task_layout)
        }
    }

    private val loadingDialog : Dialog by lazy {
        Dialog(this,R.style.DialogCustomTheme).apply {
            setupDialog(R.layout.loading_layout)
        }
    }

    private val taskViewModel : TaskViewModel by lazy {
        ViewModelProvider(this)[TaskViewModel::class.java]
    }

    private val taskRecyclerViewAdapter : TaskRecyclerViewAdapter by lazy {
        TaskRecyclerViewAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(gameBinding.root)

        gameBinding.task.adapter = taskRecyclerViewAdapter

        //Add Task
        val addCloseImg = addTaskDialog.findViewById<ImageView>(R.id.closeIcon)
        addCloseImg.setOnClickListener{addTaskDialog.dismiss()}

        val addETTitle = addTaskDialog.findViewById<TextInputEditText>(R.id.edTkTitle)
        val addETTitleL = addTaskDialog.findViewById<TextInputLayout>(R.id.ediTaskTitle)
        addETTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(addETTitle, addETTitleL)
            }
        })

        val addETDesc = addTaskDialog.findViewById<TextInputEditText>(R.id.edTkDes)
        val addETDescL = addTaskDialog.findViewById<TextInputLayout>(R.id.ediTaskDes)
        addETDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(addETDesc, addETDescL)
            }
        })

        gameBinding.addTaskFABtn.setOnClickListener{
            clearEditText(addETTitle, addETTitleL)
            clearEditText(addETDesc, addETDescL)
            addTaskDialog.show()
        }

        val saveBtn = addTaskDialog.findViewById<Button>(R.id.saveBtn)
        saveBtn.setOnClickListener {
            if (validateEditText(addETTitle, addETTitleL)
                && validateEditText(addETDesc, addETDescL)
            ) {
                addTaskDialog.dismiss()
                val newTask = Task(
                    UUID.randomUUID().toString(),
                    addETTitle.text.toString().trim(),
                    addETDesc.text.toString().trim(),
                    Date()
                )
                taskViewModel.insertTask(newTask).observe(this) {
                    when(it.status){
                        Status.LOADING -> {
                            loadingDialog.show()
                        }
                        Status.SUCCESS -> {
                            loadingDialog.dismiss()
                            if (it.data?.toInt() != -1) {
                                longToastShow("Task Added Successfully")
                            }
                        }
                        Status.ERROR -> {
                            loadingDialog.dismiss()
                            it.message?.let { it1 -> longToastShow(it1) }
                        }
                    }
                }
            }
        }

        // Update Task
        val updateCloseImg = updateTaskDialog.findViewById<ImageView>(R.id.closeIcon)
        updateCloseImg.setOnClickListener{updateTaskDialog.dismiss()}

        val updateETTitle = updateTaskDialog.findViewById<TextInputEditText>(R.id.edTkTitle)
        val updateETTitleL = updateTaskDialog.findViewById<TextInputLayout>(R.id.ediTaskTitle)
        updateETTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(updateETTitle, updateETTitleL)
            }
        })

        val updateETDesc = updateTaskDialog.findViewById<TextInputEditText>(R.id.edTkDes)
        val updateETDescL = updateTaskDialog.findViewById<TextInputLayout>(R.id.ediTaskDes)
        updateETDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(updateETDesc, updateETDescL)
            }
        })

        val updateBtn = updateTaskDialog.findViewById<Button>(R.id.updateBtn)
        updateBtn.setOnClickListener {
            if (validateEditText(addETTitle, addETTitleL)
                && validateEditText(addETDesc, addETDescL)
            ) {

                updateTaskDialog.dismiss()
                val newTask = Task(
                    UUID.randomUUID().toString(),
                    addETTitle.text.toString().trim(),
                    addETDesc.text.toString().trim(),
                    Date()
                )
            }
        }

        callGetTaskList()
        loadingDialog.show()
    }

    private fun callGetTaskList(){
        CoroutineScope(Dispatchers.Main).launch {
            taskViewModel.getTaskList().collect {
                when (it.status) {
                    Status.LOADING -> {
                        loadingDialog.show()
                    }

                    Status.SUCCESS -> {
                        it.data?.collect { taskList ->
                            loadingDialog.dismiss()
                            taskRecyclerViewAdapter.addAllTask(taskList)
                        }

                    }

                    Status.ERROR -> {
                        loadingDialog.dismiss()
                        it.message?.let { it1 -> longToastShow(it1) }
                    }
                }
            }
        }
    }
}
