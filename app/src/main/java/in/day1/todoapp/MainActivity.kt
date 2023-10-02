package `in`.day1.todoapp

import `in`.day1.todoapp.databinding.ActivityMainBinding
import `in`.day1.todoapp.databinding.DialogueUpdateBinding
import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
//        Setting the database using the TaskAPP
        val taskDao = (application as TaskApp).db.taskDao()

//        Adding by calling the function add task using the onclick listener
        binding?.btnAddTask?.setOnClickListener {
            addTask(taskDao)
        }

//        Get all the data of the task
        lifecycleScope.launch {
            taskDao.fetchAllTask().collect {
                val list = ArrayList(it)
                setUpDataToRecylclerView(list, taskDao)
            }
        }

    }

    fun addTask(taskDao: TaskDao) {
        val time = binding?.etTime?.text.toString()
        val task = binding?.etTask?.text.toString()

        if(task.isNotEmpty() && time.isNotEmpty()) {
            lifecycleScope.launch{
                taskDao.insert(TaskEntity(task=task, time = time))
                Toast.makeText(applicationContext,"Record was Saved", Toast.LENGTH_SHORT)
                    .show()
                binding?.etTask?.text?.clear()
                binding?.etTime?.text?.clear()
            }
        }else {
            Toast.makeText(applicationContext,"Task or Time Can not be empty", Toast.LENGTH_SHORT)
                .show()
        }
    }

//    Function to set the data of the Task
    private fun setUpDataToRecylclerView(taskList: ArrayList<TaskEntity>,
        taskDao: TaskDao) {
        if(taskList.isNotEmpty()) {
            val itemAdapter = ItemAdapter(taskList,
                {
                    updateID -> updateRecordDialog(updateID, taskDao)
                },
                {
                    deleteId -> deleteRecordAlertDialog(deleteId, taskDao)
                }

                )
            binding?.taskList?.layoutManager = LinearLayoutManager(this@MainActivity)
            binding?.taskList?.adapter = itemAdapter
            binding?.taskList?.visibility = View.VISIBLE
            binding?.emptyListText?.visibility = View.GONE
        }
        else {
            binding?.taskList?.visibility = View.GONE
            binding?.emptyListText?.visibility = View.VISIBLE
        }
    }

//    Function to update the record entry of the user
    private fun updateRecordDialog(id: Int, taskDao: TaskDao) {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        val binding = DialogueUpdateBinding.inflate(layoutInflater)
        updateDialog.setContentView(binding.root)

        lifecycleScope.launch {
            taskDao.fetchTaskById(id).collect{
                if(it != null) {
                    binding.etUpdateTask.setText(it.task)
                    binding.etUpdateTime.setText(it.time)
                }
            }
        }

        binding.tvUpdate.setOnClickListener {
            val time = binding.etUpdateTime.text.toString()
            val task = binding.etUpdateTask.text.toString()

            if(time.isNotEmpty() && task.isNotEmpty()) {
                lifecycleScope.launch{
                    taskDao.update(TaskEntity(id,time =time,task =  task))
                    Toast.makeText(applicationContext, "Record Updated",
                        Toast.LENGTH_SHORT ).show()
                    updateDialog.dismiss()
                }
            }
            else {
                Toast.makeText(applicationContext, "Task or Time can't be blank",
                    Toast.LENGTH_SHORT ).show()
            }
        }

        binding.tvCancel.setOnClickListener {
            updateDialog.dismiss()
        }

        updateDialog.show()
    }

//    Function to delete a record of the dialog
    private fun deleteRecordAlertDialog(id: Int, taskDao: TaskDao) {
        val deleteDialogBuilder = AlertDialog.Builder(this)
        deleteDialogBuilder.setTitle("Delete Task")
        deleteDialogBuilder.setIcon(R.drawable.deleteicon)
        deleteDialogBuilder.setPositiveButton("Yes"){dialoginterface, _ ->
            lifecycleScope.launch{
                taskDao.delete(TaskEntity(id))
                Toast.makeText(applicationContext, "Record Deleted",
                    Toast.LENGTH_SHORT ).show()
                dialoginterface.dismiss()
            }
        }
        deleteDialogBuilder.setNegativeButton("NO"){dialoginterface, _ ->
            dialoginterface.dismiss()
        }

        val deleteDialog = deleteDialogBuilder.create()
        deleteDialog.setCancelable(false)
        deleteDialog.show()
    }
}