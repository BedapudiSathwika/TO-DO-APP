package www.app.todolist_working

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.DatePicker
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

    val cldr: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo_item)

        // Set the toolbar title directly using supportActionBar
        supportActionBar?.title = "Add Item"

        // add back arrow to toolbar
        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true);
            supportActionBar?.setDisplayShowHomeEnabled(true);
        }

        dbHelper = TodoDatabaseHelper(this)

        val etItemName = findViewById<EditText>(R.id.etItemName)
        val tvDueDate = findViewById<TextView>(R.id.tvDueDate)
        val btnAddItem = findViewById<Button>(R.id.btnAddItem)

        // Show DatePickerDialog when tvDueDate is clicked
        tvDueDate.setOnClickListener {
            showDatePickerDialog(tvDueDate)
        }


        // Add item button click listener
        btnAddItem.setOnClickListener {
            val itemName = etItemName.text.toString()
            val dueDate = tvDueDate.text.toString()

            if (itemName.isNotBlank()) {
                // Insert the item into the database
                val result = dbHelper.insertTodoItem(itemName, dueDate)

                if (result != -1L) {
                    Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show()
                    finish() // Close this activity and return to previous one
                } else {
                    Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter an item name", Toast.LENGTH_SHORT).show()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Navigate back to the previous screen
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}