package com.komat.randomtodo.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.komat.randomtodo.databinding.ActivityMainBinding
import android.app.Activity
import android.view.View
import com.komat.randomtodo.datasource.TaskDataSource

//MainActivity
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.recyclervTasks.adapter = adapter
        updateList()

        insertListeners()
    }

    private fun insertListeners() {
        binding.btnFab.setOnClickListener {
            startActivityForResult(Intent(this, AddTaskActivity::class.java), CREATE_NEW_TASK)
        }

        adapter.listenerEdit = {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            startActivityForResult(intent, CREATE_NEW_TASK)
        }

        adapter.listenerDelete = {
            TaskDataSource.deleteTask(it)
            updateList()
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK) {
            //binding.recyclervTasks.adapter = adapter
            //adapter.submitList(TaskDataSource.getList())
            updateList()
        }
    }

    private fun updateList() {
        val list = TaskDataSource.getList()
        binding.includeEmpty.emptyState.visibility =
            if(list.isEmpty()) View.VISIBLE else View.GONE
        adapter.submitList(TaskDataSource.getList())
    }

    companion object {
        private const val CREATE_NEW_TASK = 1000
    }

}