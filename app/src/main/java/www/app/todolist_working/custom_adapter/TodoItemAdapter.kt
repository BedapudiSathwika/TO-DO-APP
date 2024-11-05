package www.app.todolist_working.custom_adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import www.app.todolist_working.EditTodoItemActivity
import www.app.todolist_working.R
import www.app.todolist_working.data_class.TodoItem
import www.app.todolist_working.sqlite_helper.TodoDatabaseHelper
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

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

        if (todoItem == null) return view

        // Lookup view for data population
        val checkboxComplete = view.findViewById<CheckBox>(R.id.checkboxComplete)
        val todoItemName = view.findViewById<TextView>(R.id.todoItemName)
        val todoItemDueDate = view.findViewById<TextView>(R.id.todoItemDueDate)
        val imageviewDelete = view.findViewById<ImageView>(R.id.imageviewDelete)
        val imageviewEdit = view.findViewById<ImageView>(R.id.imageviewEdit)
        val imageviewMove = view.findViewById<ImageView>(R.id.imageviewMove)

        // Populate the data into the template view using the data object
        todoItemName.text = todoItem?.name
        todoItemDueDate.text = todoItem?.dueDate // Set the due date
        checkboxComplete.isChecked = todoItem?.completed == 1

        val nearestDueDate = dbHelper.getNearestDueDateForList_Item(todoItem!!.itemId)
        if (!nearestDueDate.isNullOrEmpty()) {
            val color = getHighlightColor(nearestDueDate)
            view.setBackgroundColor(color)
        }


        // Set click listener to navigate to EditTodoItemActivity
//        view.setOnClickListener {
//
//        }

        imageviewEdit.setOnClickListener {

            val todoItemClicked = todoItem?.name

            val builder = AlertDialog.Builder(context)
            builder.setTitle("$todoItemClicked")
                .setMessage("Proceed to Edit")
                .setCancelable(false)
                .setPositiveButton("Ok") { _: DialogInterface?, _: Int ->

                    // Create an Intent to start the EditTodoItemActivity
                    val intent = Intent(context, EditTodoItemActivity::class.java).apply {
                        putExtra("ITEM_ID", todoItem?.itemId)
                        putExtra("ITEM_NAME", todoItem?.name)
                        putExtra("DUE_DATE", todoItem?.dueDate)
                    }
                    context.startActivity(intent)
                }
                .setNegativeButton(
                    "Cancel"
                ) { dialog: DialogInterface, _: Int -> dialog.dismiss() }

            //Creating dialog box
            val dialog = builder.create()
            dialog.show()
        }

        // Handle checkbox state change
        checkboxComplete.setOnCheckedChangeListener { _, isChecked ->
            todoItem?.completed = if (isChecked) 1 else 0
            todoItem?.let { dbHelper.updateCheckboxTodoItem(it) }
            listener.onItemChanged() // Notify the activity about the change
        }

        imageviewDelete.setOnClickListener {

            val todoItemClicked = todoItem.name
            val builder = AlertDialog.Builder(context)
            builder.setTitle("DELETE $todoItemClicked")
                .setMessage("Proceed to delete?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _: DialogInterface?, _: Int ->

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
                .setNegativeButton(
                    "Cancel"
                ) { dialog: DialogInterface, _: Int -> dialog.dismiss() }

            //Creating dialog box
            val dialog = builder.create()
            dialog.show()

        }

        imageviewMove.setOnClickListener {
            showMoveDialog(todoItem.itemId)
        }


        // Return the completed view to render on screen
        return view
    }

    private fun showMoveDialog(itemId: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Move To List")

        // Get the available lists from the database
        val lists = dbHelper.getTodoLists() // Make sure this method fetches all lists
        val listNames = lists.map { it.name }.toTypedArray()

        // Set up the dialog with a single-choice list
        builder.setSingleChoiceItems(listNames, -1) { dialog, which ->
            // Get the selected list ID
            val selectedListId = lists[which].listId

            // Move the item to the selected list
            if (dbHelper.moveTodoItemToList(itemId, selectedListId)) {
                Toast.makeText(context, "Item moved successfully", Toast.LENGTH_SHORT).show()
                listener.onItemChanged() // Notify the activity about the change
            } else {
                Toast.makeText(context, "Failed to move item", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }


    fun getHighlightColor(dueDate: String): Int {
        val today = LocalDate.now()

        // Define the date formatter matching the format of your date strings
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")

        return try {
            // Parse the due date with the specified formatter
            val due = LocalDate.parse(dueDate, formatter)

            when {
                due.isEqual(today) -> Color.YELLOW
                due.isBefore(today) -> Color.RED
                else -> Color.TRANSPARENT
            }
        } catch (e: DateTimeParseException) {
            // Handle parse exception, log if necessary
            Color.TRANSPARENT // Return default color if parsing fails
        }
    }

}
