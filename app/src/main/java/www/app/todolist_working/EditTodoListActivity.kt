package www.app.todolist_working

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import www.app.todolist_working.sqlite_helper.TodoDatabaseHelper

class EditTodoItemActivity : AppCompatActivity() {

    private lateinit var dbHelper: TodoDatabaseHelper
    private lateinit var editTextItemName: EditText
    private lateinit var textViewDueDate: TextView
    private var itemId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_todo_list)

        dbHelper = TodoDatabaseHelper(this)
        editTextItemName = findViewById(R.id.editTextListName)
        textViewDueDate = findViewById(R.id.textViewDueDate)

        // Get the item ID and details from the intent
        itemId = intent.getIntExtra("ITEM_ID", -1)
        val currentItemName = intent.getStringExtra("ITEM_NAME")
        val currentDueDate = intent.getStringExtra("DUE_DATE")

        editTextItemName.setText(currentItemName)
        textViewDueDate.setText(currentDueDate)

        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            saveItem()
        }
    }

    private fun saveItem() {
        val newName = editTextItemName.text.toString()
        val newDueDate = textViewDueDate.text.toString()
        dbHelper.editTodoItem(itemId, newName, newDueDate)
        Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show()
        finish()
    }
}
