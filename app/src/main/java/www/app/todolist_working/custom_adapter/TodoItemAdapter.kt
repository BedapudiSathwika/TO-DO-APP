package www.app.todolist_working.custom_adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import www.app.todolist_working.R
import www.app.todolist_working.data_class.TodoItem
import www.app.todolist_working.sqlite_helper.TodoDatabaseHelper

class TodoItemAdapter(
    context: Context,
    private var items: List<TodoItem>,
    private val dbHelper: TodoDatabaseHelper,
    private val listener: OnTodoItemChangeListener
) : ArrayAdapter<TodoItem>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get the data item for this position
        val todoItem = getItem(position)

        // Check if an existing view is being reused, otherwise inflate the view
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_todo, parent, false)

        // Lookup view for data population
        val checkboxComplete = view.findViewById<CheckBox>(R.id.checkboxComplete)
        val todoItemName = view.findViewById<TextView>(R.id.todoItemName)
        val todoItemDueDate = view.findViewById<TextView>(R.id.todoItemDueDate)
        val imageviewDelete = view.findViewById<ImageView>(R.id.imageviewDelete)

        // Populate the data into the template view using the data object
        todoItemName.text = todoItem?.name
        todoItemDueDate.text = todoItem?.dueDate // Set the due date
        checkboxComplete.isChecked = todoItem?.completed == 1

        // Handle checkbox state change
        checkboxComplete.setOnCheckedChangeListener { _, isChecked ->
            todoItem?.completed = if (isChecked) 1 else 0
            todoItem?.let { dbHelper.updateCheckboxTodoItem(it) }
            listener.onItemChanged() // Notify the activity about the change
        }

        imageviewDelete.setOnClickListener {
            // Ensure todoItem is not null before accessing its properties
            if (todoItem != null) {
                // Call the delete method on the database helper
                if (dbHelper.deleteTodoItem(todoItem.itemId)) {
                    Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show()

                    // Remove the item from the list and refresh the adapter
                    items = items.filter { it.itemId != todoItem.itemId } // Filter out the deleted item
                    notifyDataSetChanged() // Notify the adapter about the data change
                    listener.onItemChanged() // Notify the activity about the change
                } else {
                    Toast.makeText(context, "Failed to remove item", Toast.LENGTH_SHORT).show()
                }
            }
        }


        // Return the completed view to render on screen
        return view
    }
}
