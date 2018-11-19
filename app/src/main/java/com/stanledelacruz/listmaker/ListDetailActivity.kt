package com.stanledelacruz.listmaker

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.widget.EditText

class ListDetailActivity : AppCompatActivity() {

    private lateinit var list: TaskList
    private lateinit var listItemsRecyclerView: RecyclerView
    private lateinit var addTaskList: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_detail)

        list = intent.getParcelableExtra(ListActivity.INTENT_LIST_KEY)
        title = list.name

        listItemsRecyclerView = findViewById(R.id.list_items_recyclerview)
        listItemsRecyclerView.adapter = ListItemsRecyclerViewAdapter(list)
        listItemsRecyclerView.layoutManager = LinearLayoutManager(this)

        addTaskList = findViewById(R.id.add_task_button)
        addTaskList.setOnClickListener { showCreateDialog() }
    }

    private fun showCreateDialog() {

        val taskEditText = EditText(this)
        taskEditText.inputType = InputType.TYPE_CLASS_TEXT

        AlertDialog.Builder(this)
            .setTitle(R.string.task_to_add).setView(taskEditText).setPositiveButton(R.string.add_task) { dialog, _ ->

                val task = taskEditText.text.toString()
                list.tasks.add(task)

                val recyclerViewAdapter = listItemsRecyclerView.adapter as ListItemsRecyclerViewAdapter
                recyclerViewAdapter.notifyItemInserted(list.tasks.size)
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun onBackPressed() {
        val bundle = Bundle()
        bundle.putParcelable(ListActivity.INTENT_LIST_KEY, list)

        val intent = Intent()
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }
}
