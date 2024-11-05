package www.app.todolist_working

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import www.app.todolist_working.sqlite_helper.TodoDatabaseHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTodoIListActivity : AppCompatActivity() {

    private lateinit var dbHelper: TodoDatabaseHelper
    private var listIdFk: Int = -1 // Initialize to a default value

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo_list)

        // Set the toolbar title directly using supportActionBar
        supportActionBar?.title = "Add ToDo List"

        // add back arrow to toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        dbHelper = TodoDatabaseHelper(this)

        // Get the listIdFk from intent extras
        listIdFk = intent.getIntExtra("LIST_ID", -1)

        val etItemName = findViewById<EditText>(R.id.etItemName)
        val btnAddItem = findViewById<Button>(R.id.btnAddItem)

        // Add item button click listener
        btnAddItem.setOnClickListener {
            val itemName = etItemName.text.toString().trim()

            if(itemName == "") {

                Toast.makeText(this, "Please input a name for the note", Toast.LENGTH_SHORT).show()

            } else if (listIdFk != -1) { // Ensure that the list ID is valid
                val result = dbHelper.insertTodoList(itemName) // Include listIdFk here
                if (result != -1L) {
                    Toast.makeText(this, "Todo item added successfully!", Toast.LENGTH_SHORT).show()
                    finish() // Optionally finish this activity to go back
                } else {
                    Toast.makeText(this, "Error adding todo item.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Invalid List ID.", Toast.LENGTH_SHORT).show()
            }
        }
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