package www.app.todolist_working

import android.app.DatePickerDialog
import android.os.Bundle
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

class AddTodoItemActivity : AppCompatActivity() {

    private lateinit var dbHelper: TodoDatabaseHelper
    private var listIdFk: Int = -1 // Initialize to a default value

    private val cldr: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo_item)

        // Set the toolbar title directly using supportActionBar
        supportActionBar?.title = "Add Item"

        // add back arrow to toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        dbHelper = TodoDatabaseHelper(this)

        // Get the listIdFk from intent extras
        listIdFk = intent.getIntExtra("LIST_ID", -1)

        val etItemName = findViewById<EditText>(R.id.etItemName)
        val tvDueDate = findViewById<TextView>(R.id.tvDueDate)
        val btnAddItem = findViewById<Button>(R.id.btnAddItem)

        // Show DatePickerDialog when tvDueDate is clicked
        tvDueDate.setOnClickListener {
            showDatePickerDialog(tvDueDate)
        }

        // Add item button click listener
        btnAddItem.setOnClickListener {
            val itemName = etItemName.text.toString().trim()
            val dueDate = tvDueDate.text.toString().trim()

            if(itemName == "") {

                Toast.makeText(this, "Please input a name for the note", Toast.LENGTH_SHORT).show()

            } else if (listIdFk != -1) { // Ensure that the list ID is valid
                val result = dbHelper.insertTodoItem(itemName, dueDate, listIdFk) // Include listIdFk here
                if (result != -1L) {
                    Toast.makeText(this, "Todo item added successfully!", Toast.LENGTH_SHORT).show()
                    finish() // Optionally finish this activity to go back
                } else {
                    Toast.makeText(this, "Item already exists.", Toast.LENGTH_SHORT).show()
                    finish() // Optionally finish this activity to go back
                }
            } else {
                Toast.makeText(this, "Invalid List ID.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePickerDialog(tvDueDate: TextView) {
        val day = cldr.get(Calendar.DAY_OF_MONTH)
        val month = cldr.get(Calendar.MONTH)
        val year = cldr.get(Calendar.YEAR)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                cldr.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                tvDueDate.text = dateFormat.format(cldr.time)
            },
            year, month, day
        )
        datePickerDialog.show()
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