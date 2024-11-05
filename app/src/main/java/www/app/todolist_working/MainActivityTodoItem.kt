package www.app.todolist_working

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View

import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import www.app.todolist_working.custom_adapter.OnTodoItemChangeListener
import www.app.todolist_working.custom_adapter.TodoItemAdapter
import www.app.todolist_working.data_class.TodoItem
import www.app.todolist_working.sqlite_helper.TodoDatabaseHelper


class MainActivityTodoItem : AppCompatActivity(), OnTodoItemChangeListener {

    private lateinit var dbHelper: TodoDatabaseHelper
    private lateinit var listViewTodoLists: ListView
    private lateinit var emptyView: TextView

    private lateinit var todoCounter: TextView
    private lateinit var completedCounter: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_todo_item) // Ensure this matches your XML file name

        // Set the toolbar title directly using supportActionBar
        supportActionBar?.title = "Add To Do Item"

        // add back arrow to toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        dbHelper = TodoDatabaseHelper(this)

        val btnAddList = findViewById<Button>(R.id.btnAddList)
        listViewTodoLists = findViewById(R.id.listViewTodoLists)
        emptyView = findViewById(android.R.id.empty)

        todoCounter = findViewById(R.id.todoCounter)
        completedCounter = findViewById(R.id.completedCounter)

        btnAddList.setOnClickListener {
            // Trigger activity to add new ToDo item
            val intent = Intent(this, AddTodoItemActivity::class.java)
            intent.putExtra("LIST_ID", 0) // Adjust if necessary
            startActivity(intent)
        }

        // Load items from database
        loadTodoItems()
        dbHelper.logAllData() // Log all data from the database
        dbHelper.logTableSchema()

        loadCounters()
    }

    private fun loadCounters() {
        val totalItems = dbHelper.getTodoCount()
        val completedItems = dbHelper.getCompletedTodoCount()
        todoCounter.text = "Total Items: $totalItems"
        completedCounter.text = "Completed Items: $completedItems"
    }

    private fun loadTodoItems() {
        // Fetch all todo items from the database
        val todoItems: List<TodoItem> = dbHelper.getAllTodoItems()

        val listViewTodoItems = findViewById<ListView>(R.id.listViewTodoLists)
        val emptyView = findViewById<TextView>(android.R.id.empty)

        if (todoItems.isEmpty()) {
            listViewTodoItems.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
        } else {
            listViewTodoItems.visibility = View.VISIBLE
            emptyView.visibility = View.GONE

            // Set up custom adapter for ListView, passing the dbHelper
            val adapter = TodoItemAdapter(this, todoItems, dbHelper,this)
            listViewTodoItems.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        loadTodoItems() // Reload the lists when returning to this activity
    }

    override fun onItemChanged() {
        loadCounters() // Refresh counters
        loadTodoItems() // Refresh the todo list if needed
    }

    //Back button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish() // Navigate back to the previous screen
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}

