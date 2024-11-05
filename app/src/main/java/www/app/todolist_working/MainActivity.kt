package www.app.todolist_working

import android.content.Intent
import android.os.Bundle
import android.view.View

import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import www.app.todolist_working.custom_adapter.OnTodoItemChangeListener
import www.app.todolist_working.custom_adapter.TodoItemAdapter
import www.app.todolist_working.custom_adapter.TodoListAdapter
import www.app.todolist_working.data_class.TodoItem
import www.app.todolist_working.sqlite_helper.TodoDatabaseHelper


class MainActivity : AppCompatActivity(), OnTodoItemChangeListener {

    private lateinit var dbHelper: TodoDatabaseHelper
    private lateinit var listViewTodoLists: ListView
    private lateinit var emptyView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Ensure this matches your XML file name

        dbHelper = TodoDatabaseHelper(this)

        val btnAddList = findViewById<Button>(R.id.btnAddList)
        listViewTodoLists = findViewById(R.id.listViewTodoLists)
        emptyView = findViewById(android.R.id.empty)

        btnAddList.setOnClickListener {
            // Trigger activity to add new ToDo item
            val intent = Intent(this, AddTodoItemActivity::class.java)
            intent.putExtra("LIST_ID", 0) // Adjust if necessary
            startActivity(intent)
        }

        // Load items from database
        loadTodoList()
//        dbHelper.logAllData() // Log all data from the database
//        dbHelper.logTableSchema()

    }



    private fun loadTodoList() {
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
            val adapter = TodoListAdapter(this, todoItems, dbHelper,this)
            listViewTodoItems.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        loadTodoList() // Reload the lists when returning to this activity
    }

    override fun onItemChanged() {
        loadTodoList() // Refresh the todo list if needed
    }

}

