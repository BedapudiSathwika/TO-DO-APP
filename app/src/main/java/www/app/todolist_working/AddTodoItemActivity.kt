package www.app.todolist_working

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import www.app.todolist_working.sqlite_helper.TodoDatabaseHelper

class AddTodoItemActivity : AppCompatActivity() {

    private lateinit var dbHelper: TodoDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo_item)

        dbHelper = TodoDatabaseHelper(this)

        val btnAddItem = findViewById<Button>(R.id.btnAddItem)
        btnAddItem.setOnClickListener {
            val itemName = findViewById<EditText>(R.id.etItemName).text.toString()
            val dueDate = findViewById<EditText>(R.id.etDueDate).text.toString()
            val listId = intent.getIntExtra("LIST_ID", -1)

            if (itemName.isNotEmpty()) {
                // Code to insert item into SQLite database
            }
        }

    }
}