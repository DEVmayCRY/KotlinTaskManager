package com.komat.randomtodo.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.komat.randomtodo.databinding.ActivityAddTaskBinding
import com.komat.randomtodo.datasource.TaskDataSource
import com.komat.randomtodo.extensions.format
import com.komat.randomtodo.extensions.text
import com.komat.randomtodo.model.Task
import java.util.*


class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)



        if (intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                binding.tilTitulo.text = it.title
                binding.tilDate.text = it.title
                binding.tilHora.text = it.title
                binding.tilDescricao.text = it.title
            }
        }

        insertListeners()
    }

    private fun insertListeners() {
        binding.tilDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offSet = timeZone.getOffset(Date(it).time) * -1
                binding.tilDate.text = Date(it + offSet).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        binding.tilHora.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()
            timePicker.addOnPositiveButtonClickListener {
                val minute =
                    if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour

                binding.tilHora.text = "$hour:$minute"
            }
            timePicker.show(supportFragmentManager, null)
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }

        fun checkButtom(): String {
            var tipo = ""
            if (binding.radioButton.isChecked){
                binding.radioButton2.isChecked = false
                binding.radioButton3.isChecked = false
                tipo = "um"
            }else if (binding.radioButton2.isChecked){
                binding.radioButton.isChecked = false
                binding.radioButton3.isChecked = false
                tipo = "dois"
            }else if (binding.radioButton3.isChecked){
                binding.radioButton.isChecked = false
                binding.radioButton2.isChecked = false
                tipo = "tres"
            }
            return tipo
        }

        binding.btnNovaTarefa.setOnClickListener {
            val task = Task(
                title = binding.tilTitulo.text,
                date = binding.tilDate.text,
                hour = binding.tilHora.text,
                description = binding.tilDescricao.text,
                type = checkButtom(),
                id = intent.getIntExtra(TASK_ID, 0)
            )
            TaskDataSource.insertTask(task)

            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    companion object {
        const val TASK_ID = "task_id"
    }

}