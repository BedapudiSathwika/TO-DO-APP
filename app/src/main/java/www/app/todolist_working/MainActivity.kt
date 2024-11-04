package www.app.todolist_working

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
        // Code to fetch lists from SQLite database and display
    }
}