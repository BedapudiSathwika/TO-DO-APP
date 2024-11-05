package www.app.todolist_working.custom_adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import www.app.todolist_working.EditTodoItemActivity
import www.app.todolist_working.MainActivityTodoItem
import www.app.todolist_working.R
import www.app.todolist_working.data_class.TodoItem
import www.app.todolist_working.sqlite_helper.TodoDatabaseHelper


class TodoListAdapter(
    context: Context,
    private var items: List<TodoItem>,
    private val dbHelper: TodoDatabaseHelper,
    private val listener: OnTodoItemChangeListener
) : ArrayAdapter<TodoItem>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get the data item for this position
        val todoItem = getItem(position)

        // Check if an existing view is being reused, otherwise inflate the view
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_todo_list, parent, false)

        if (todoItem == null) return view

        // Lookup view for data population
        val todoListName = view.findViewById<TextView>(R.id.todoListName)
        val todoListDueDate = view.findViewById<TextView>(R.id.todoListDueDate)
        val btnAddMain = view.findViewById<Button>(R.id.btnAddMain)


        // Populate the data into the template view using the data object
        todoListName.text = todoItem?.name
       // todoListDueDate.text = todoItem?.dueDate // Set the due date

        val nearestDueDate = dbHelper.getNearestDueDateForList(todoItem!!.itemId)
        if (!nearestDueDate.isNullOrEmpty()) {
            todoListDueDate.text = nearestDueDate
//            val color = getHighlightColor(nearestDueDate)
//            view.setBackgroundColor(color)
        }

        btnAddMain.setOnClickListener{
            val intent = Intent(context, MainActivityTodoItem::class.java).apply {
//                        putExtra("ITEM_ID", todoItem?.itemId)
//                        putExtra("ITEM_NAME", todoItem?.name)
//                        putExtra("DUE_DATE", todoItem?.dueDate)
            }
            context.startActivity(intent)
        }

        // Set click listener to navigate to EditTodoItemActivity
//        view.setOnClickListener {
//
//            val todoItemClicked = todoItem?.name
//
//            val builder = AlertDialog.Builder(context)
//            builder.setTitle("$todoItemClicked")
//                .setMessage("Proceed to View / Edit List")
//                .setCancelable(false)
//                .setPositiveButton("Ok") { _: DialogInterface?, _: Int ->
//                    // Create an Intent to start the EditTodoItemActivity
//                    val intent = Intent(context, MainActivityTodoItem::class.java).apply {
////                        putExtra("ITEM_ID", todoItem?.itemId)
////                        putExtra("ITEM_NAME", todoItem?.name)
////                        putExtra("DUE_DATE", todoItem?.dueDate)
//                    }
//                    context.startActivity(intent)
//                }
//                .setNegativeButton(
//                    "Cancel"
//                ) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
//
//            //Creating dialog box
//            val dialog = builder.create()
//            dialog.show()
//
//
//        }

        // Return the completed view to render on screen
        return view
    }


}
