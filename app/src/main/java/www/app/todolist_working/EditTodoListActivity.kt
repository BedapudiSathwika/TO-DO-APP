package www.app.todolist_working

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import www.app.todolist_working.sqlite_helper.TodoDatabaseHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditTodoItemActivity : AppCompatActivity() {

    private lateinit var dbHelper: TodoDatabaseHelper
    private lateinit var editTextItemName: EditText
    private lateinit var textViewDueDate: TextView
    private var itemId: Int = -1

    private val cldr: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_todo_list)

        // Set the toolbar title directly using supportActionBar
        supportActionBar?.title = "Edit Item"

        // add back arrow to toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        dbHelper = TodoDatabaseHelper(this)
        editTextItemName = findViewById(R.id.editTextListName)
        textViewDueDate = findViewById(R.id.textViewDueDate)

        // Get the item ID and details from the intent
        itemId = intent.getIntExtra("ITEM_ID", -1)
        val currentItemName = intent.getStringExtra("ITEM_NAME")
        val currentDueDate = intent.getStringExtra("DUE_DATE")

        editTextItemName.setText(currentItemName)
        textViewDueDate.setText(currentDueDate)

        // Show DatePickerDialog when tvDueDate is clicked
        textViewDueDate.setOnClickListener {
            showDatePickerDialog(textViewDueDate)
        }

        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            saveItem()
        }
    }

    private fun showDatePickerDialog(textViewDueDate: TextView) {
        val day = cldr.get(Calendar.DAY_OF_MONTH)
        val month = cldr.get(Calendar.MONTH)
        val year = cldr.get(Calendar.YEAR)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                cldr.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                textViewDueDate.text = dateFormat.format(cldr.time)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun saveItem() {
        val newName = editTextItemName.text.toString()
        val newDueDate = textViewDueDate.text.toString()
        dbHelper.editTodoItem(itemId, newName, newDueDate)
        Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show()
        finish()
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
