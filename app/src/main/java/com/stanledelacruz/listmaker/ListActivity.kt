package com.stanledelacruz.listmaker

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.FrameLayout

import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity(), ListSelectionFragment.OnListItemFragmentInteractionListener {

    companion object {
        val INTENT_LIST_KEY = "list"
        val LIST_DETAIL_REQUEST_CODE = 123
    }

    private var largeScreen = false
    private var listFragment: ListDetailFragment? = null
    private var listSelectionFragment: ListSelectionFragment = ListSelectionFragment.newInstance()
    private var fragmentContainer: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        setSupportActionBar(toolbar)
        setupViews()
    }

    private fun setupViews() {

        listSelectionFragment = supportFragmentManager.findFragmentById(R.id.list_selection_fragment) as ListSelectionFragment
        fragmentContainer = findViewById(R.id.fragment_container)

        largeScreen = fragmentContainer != null

        fab.setOnClickListener { view ->
            showCreateListDialog()
        }
    }

    private fun showCreateListDialog() {
        val taskEditText = EditText(this)
        taskEditText.inputType = InputType.TYPE_CLASS_TEXT

        AlertDialog.Builder(this)
            .setTitle(R.string.name_of_list)
            .setView(taskEditText).setPositiveButton(R.string.create_list) { dialog, _ ->
                val list = TaskList(taskEditText.text.toString())
                listSelectionFragment?.addList(list)
                dialog.dismiss()
                showListDetail(list)
            }.create().show()
    }

    private fun showListDetail(list: TaskList) {
        if (!largeScreen) {
            val listDetailIntent = Intent(this, ListDetailActivity::class.java)
            listDetailIntent.putExtra(INTENT_LIST_KEY, list)
            startActivityForResult(listDetailIntent, LIST_DETAIL_REQUEST_CODE)
        } else {
            title = list.name
            listFragment = ListDetailFragment.newInstance(list)

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, listFragment, getString(R.string.list_fragment_tag))
                .addToBackStack(null)
                .commit()

            fab.setOnClickListener {
                showCreateTaskDialog()
            }

        }
    }

    private fun showCreateTaskDialog() {
        val taskEditText = EditText(this)
        taskEditText.inputType = InputType.TYPE_CLASS_TEXT
        AlertDialog.Builder(this)
            .setTitle(R.string.task_to_add)
            .setView(taskEditText)
            .setPositiveButton(R.string.add_task) { dialog, _ ->
                val task = taskEditText.text.toString()
                listFragment?.addTask(task)
                dialog.dismiss()
            }
            .create()
            .show() }

    override fun onBackPressed() {
        super.onBackPressed()

        title = resources.getString(R.string.app_name)

        listFragment?.list?.let {
            listSelectionFragment.listDataManager.saveList(it)
        }

        if (listFragment != null) {
            supportFragmentManager.beginTransaction().remove(listFragment).commit()
            listFragment = null
        }

        fab.setOnClickListener {
            showCreateListDialog()
        }
    }

    override fun onListItemClicked(list: TaskList) {
        showListDetail(list)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LIST_DETAIL_REQUEST_CODE) {
            data?.let {
                listSelectionFragment.saveList(data.getParcelableExtra<TaskList>(INTENT_LIST_KEY))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
    //endregion
}
