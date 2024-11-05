package www.app.todolist_working

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import www.app.todolist_working.sqlite_helper.TodoDatabaseHelper

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: TodoDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        dbHelper = TodoDatabaseHelper(this)

        val btnAddList = findViewById<Button>(R.id.btnAddList)
        btnAddList.setOnClickListener {
            // Trigger activity to add new ToDo list
            val intent = Intent(this, AddTodoItemActivity::class.java)
            startActivity(intent)
        }

        // Load lists from database
        loadTodoLists()
    }

    private fun loadTodoLists() {
        // Fetch lists from SQLite database
        val todoLists: List<String> = dbHelper.getTodoLists() // Use the new method

        val listViewTodoLists = findViewById<ListView>(R.id.listViewTodoLists)
        val emptyView = findViewById<TextView>(android.R.id.empty) // Reference to the empty view

        if (todoLists.isEmpty()) {
            // No items in the list
            listViewTodoLists.visibility = View.GONE // Hide the ListView
            emptyView.visibility = View.VISIBLE // Show the empty TextView
        } else {
            // Items found, display them in the ListView
            listViewTodoLists.visibility = View.VISIBLE // Show the ListView
            emptyView.visibility = View.GONE // Hide the empty TextView

            // Set up adapter for ListView
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, todoLists)
            listViewTodoLists.adapter = adapter
        }
    }

}

