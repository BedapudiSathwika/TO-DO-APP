package www.app.todolist_working.custom_adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import www.app.todolist_working.R
import www.app.todolist_working.data_class.TodoItem
import www.app.todolist_working.sqlite_helper.TodoDatabaseHelper

class TodoItemAdapter(context: Context,
                      private val items: List<TodoItem>,
                      private val dbHelper: TodoDatabaseHelper
) : ArrayAdapter<TodoItem>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get the data item for this position
        val todoItem = getItem(position)

        // Check if an existing view is being reused, otherwise inflate the view
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_todo, parent, false)

        // Lookup view for data population
        val checkboxComplete = view.findViewById<CheckBox>(R.id.checkboxComplete)
        val todoItemName = view.findViewById<TextView>(R.id.todoItemName)

        // Populate the data into the template view using the data object
        todoItemName.text = todoItem?.name
        checkboxComplete.isChecked = todoItem?.completed == 1

        // Handle checkbox state change
        checkboxComplete.setOnCheckedChangeListener { _, isChecked ->
            todoItem?.completed = if (isChecked) 1 else 0
            todoItem?.let { dbHelper.updateTodoItem(it) } // Update the database here
        }

        // Return the completed view to render on screen
        return view
    }
}